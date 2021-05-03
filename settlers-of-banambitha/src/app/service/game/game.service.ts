import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Connectable, WsService } from '../ws/ws.service';
import { UserService } from '../user/user.service';
import { UserCardInfo } from '../room/room.service';

/**
 * Colores de los jugadores
 */
export enum Color {
  AMARILLO = "Amarillo", 
  ROJO     = "Rojo", 
  VERDE    = "Verde", 
  AZUL     = "Azul"
}


/**
 * Tipos de asentamientos (valores para un 
 * posible asentamiento del tablero)
 */
export enum TipoAsentamiento {
  NADA = "Nada", 
  POBLADO_AMARILLO = "PobladoAmarillo", 
  POBLADO_AZUL     = "PobladoAzul", 
  POBLADO_VERDE    = "PobladoVerde", 
  POBLADO_ROJO     = "PobladoRojo", 
  CIUDAD_AMARILLO  = "CiudadAmarillo", 
  CIUDAD_AZUL      = "CiudadAzul", 
  CIUDAD_VERDE     = "CiudadVerde", 
  CIUDAD_ROJO      = "CiudadRojo", 
}


/**
 * Tipos de terreno según la materia 
 * prima que ofrecen 
 */
export enum TipoTerreno {
  BOSQUE   = "Bosque",
  PASTO    = "Pasto", 
  SEMBRADO = "Sembrado", 
  CERRO    = "Cerro", 
  MONTANYA = "Montanya", 
  DESIERTO = "Desierto", 
  VACIO    = "Vacio" 
}


/**
 * Tipos de caminos
 */
export enum TipoCamino {
  NADA     = "Nada", 
  ROJO     = "CaminoRojo", 
  VERDE    = "CaminoVerde", 
  AZUL     = "CaminoAzul", 
  AMARILLO = "CaminoAmarillo"
}


/**
 * Códigos de error
 * TODO: implementar códigos de error
 */
enum CodigoError {
}


/**
 * Cartas de un jugador
 */
export interface CartasJugador {
  D1: number, 
  D2: number, 
  D3: number, 
  D4: number, 
  D5: number, 
  E1: number, 
  E2: number, 
}


/**
 * Recursos de un jugador 
 */
export interface RecursosJugador {
  madera:   number, 
  piedra:   number, 
  ladrillo: number, 
  lana:     number, 
  cereales: number
}


/**
 * Información completa de un jugador
 */
export interface Jugador {
  nombre:   String,
  turno:    number, 
  color:    Color,
  puntos:   number, 
  cartas:   CartasJugador,
  recursos: RecursosJugador
}


/**
 * Mensaje que se puede recibir durante una partida
 */
export interface Mensaje {
  remitente: String, 
  body:      String
}


/**
 * Información sobre TODOS los hexágonos
 * de un tablero
 */
export interface Hexagonos {
  tipo:   Array<TipoTerreno>,
  valor:  Array<number>,
  ladron: number
}


/**
 * Información sobre TODOS los vértices
 * de un tablero
 */
export interface Vertices {
  asentamiento: Array<TipoAsentamiento>, 
  posible_asentamiento: Array<Boolean>
}


/**
 * Información sobre TODAS las aristas
 * de un tablero
 */
export interface Aristas {
  camino:         Array<TipoCamino>, 
  posible_camino: Array<Boolean>,
  puerto:         Array<number>
}


/**
 * Almacena toda la información del tablero de la partida
 */
export interface Tablero {
  hexagonos: Hexagonos,
  vertices:  Vertices, 
  aristas:   Aristas
}


/**
 * Almacena toda la información necesaria de una partida
 */
export interface Partida {
  id:              String,
  miTurno:         number,
  turnoActual:     number,
  tablero:         Tablero,
  resultadoTirada: number, 
  jugadores:       Array<Jugador>,
  mensajes:        Array<Mensaje>
}


@Injectable({
  providedIn: 'root'
})
/**
 * Clase que gestiona la interacción del usuario durante una partida
 */
export class GameService implements Connectable{

  private static readonly coloresPorId: Array<Color> = [Color.AZUL, Color.ROJO, Color.AMARILLO, Color.VERDE]

  public partida: Partida

  //Identificadores de los topics a los que se suscribe 
  //el jugador
  private test_partida_topic_id:  any
  private partida_act_topic_id:   any
  private partida_chat_topic_id:  any
  private partida_com_topic_id:   any

  //Cliente de stomp
  private stompClient: any


  /**
   * Constructor. Se suscribe a los topics necesarios para poder gestionar
   * toda la dinámica juego.
   * @param wsService   servicio de websockets a utilzar (singleton)
   * @param router      router de la aplicación
   * @param userService servicio de usuario a utilizar (singleton)
   */
  constructor(private wsService: WsService, private router: Router, private userService: UserService) {
    if ( ! wsService.atatchConnectable(this)){
      this.onConnect();
    }
  }


  /**
   * Función a llamar cuando la conexión websocket con el 
   * servidor se haya establecido
   */
  public onConnect(): void {
    this.stompClient = this.wsService.getStompClient() 
  }


  /**
   * Inicia una partida de prueba
   * Solo útil para test
   */
  public comenzarPartidaPrueba(){
    let that = this
    this.test_partida_topic_id = this.stompClient.subscribe(WsService.partida_test_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        let msg: Object = JSON.parse(message.body)
        console.log(msg)
        console.log("Comenzando partida de prueba...")
        that.comenzarPartida(msg["game"], [that.userService.getUsername(), "Some1" ,"Some2", "Some3"])

      }else{
        console.log("Error crítico")
      }
    });

    //Solicitar inicio de partida
    this.stompClient.send(WsService.partidaTestComenzar, {}, JSON.stringify({from: this.userService.getUsername()}) )
  }


  /**
   * Se conecta a una partica (puede ser una partida ya en curso)
   * 
   * @param idPartida identificador de la partida
   * @param jugadores jugadores participantes (deben ser 4)
   * @Return true si ha podido inicializar la partida, false en 
   * caso contrario
   */
  public comenzarPartida(idPartida: string, jugadores: Array<String>): boolean{
    console.log("Iniciando partida...")
    let miTurno: number = jugadores.indexOf(this.userService.getUsername())
    if ( jugadores.length == 4 && miTurno >= 0){
      this.partida = {
        miTurno: miTurno + 1,
        id: idPartida, 
        jugadores: this.inicializarJugadores(jugadores),
        turnoActual: 0, 
        tablero: null, 
        resultadoTirada: 0, 
        mensajes: []
      }

      this.subscribeToTopics()

      return true
    }

    return false
  }


  /**
   * Inicializa los jugadores de una partida suponiendo que la partida 
   * está en el estado inicial (nadie ha jugado todavía). 
   * 
   * @param jugadores nombres de los jugadores, en orden de turno
   * PRE: jugadores.length = 4
   */
  private inicializarJugadores(jugadores: Array<String>): Array<Jugador>{
    let jugadoresPartida: Array<Jugador> = []
    for (let i = 0; i < jugadores.length; i++){
      jugadoresPartida[i] = {
        nombre: jugadores[i],
        turno: i + 1, 
        color: GameService.coloresPorId[i],
        puntos: 0, 
        recursos: { madera: 0, ladrillo: 0, cereales: 0, lana: 0, piedra: 0 },
        cartas: { D1: 0, D2: 0, D3: 0, D4: 0, D5: 0, E1: 0, E2: 0}
      }
    }
    return jugadoresPartida
  }


  /**
   * Se suscribe a los topics necesarios para jugar una partida. 
   * Solo se realizan las subscripciones si se ha almacenado el identificador
   * de partida.
   */
  private subscribeToTopics(): void {
    if ( this.partida != null && this.partida.id != null){
      let that = this

      //Suscripción a los mensajes de la partida (acciones de los jugadores)
      this.partida_act_topic_id = this.stompClient.subscribe(WsService.partida_act_topic + this.partida.id,
      function (message) {
        if (message.body){
          that.procesarMensajePartida(JSON.parse(message.body))
        }else{
          console.log("Error crítico")
        }
      });
      
      //Suscripción al chat de la partida
      this.partida_chat_topic_id = this.stompClient.subscribe(WsService.partida_chat_topic + this.partida.id,
      function (message) {
        if (message.body){
          that.procesarMensajeChat(JSON.parse(message.body))
        }else{
          console.log("Error crítico")
        }
      });

      //Suscripción a las peticiones de comercio
      this.partida_com_topic_id = this.stompClient.subscribe(WsService.partida_com_topic + this.partida.id,
      function (message) {
        if (message.body){
          that.procesarMensajeComercio(JSON.parse(message.body))
        }else{
          console.log("Error crítico")
        }
      });
    }
  }


  /**
   * Procesa un mensaje de partida (con información del tablero)
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajePartida(msg: Object): void{
    console.log(msg)
  }


  /**
   * Procesa un mensaje del chat de la partida
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajeChat(msg: Object): void{
    console.log(msg)
  }


  /**
   * Procesa un mensaje de comercio
   * 
   * @param msg 
   */
  private procesarMensajeComercio(msg: Object): void{
    console.log(msg)
  }


  /**
   * Finaliza la partida actual (solo en el lado del cliente)
   */
  private finalizarpartida() {
    this.partida = null
    this.partida_act_topic_id.unsubscribe()
    this.partida_com_topic_id.unsubscribe()
    this.partida_chat_topic_id.unsubscribe()
  }

}
