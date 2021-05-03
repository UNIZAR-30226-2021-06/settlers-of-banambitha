import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Connectable, WsService } from '../ws/ws.service';
import { UserService } from '../user/user.service';
import { UserCardInfo } from '../room/room.service';


/**
 * Algunos de los identificadores del mensaje de respuesta
 * del backend son un poco raros, así que esta clase
 * formaliza los identificadores y facilita cambiarlos
 * si es que se cambian desde backend en algún momento.
 * Ej: posibles_camino????
 */
enum MessageKeys {
  MENSAJE                         = "Mensaje",
  EXIT_STATUS                     = "exit_status", 
  TAB_INFO                        = "Tab_inf", 
  TAB_INFO_HEXAGONOS              = "hexagono",
  HEXAGONOS_TIPOS                 = "tipo",
  HEXAGONOS_VALORES               = "valor",
  HEXAGONOS_LADRON                = "ladron",
  TAB_INFO_VERTICES               = "vertices",
  VERTICES_ASENTAMIENTOS          = "asentamiento",
  VERTICES_POSIBLES_ASENTAMIENTOS = "posibles_asentamiento",
  TAB_INFO_ARISTAS                = "aristas", 
  ARISTAS_CAMINOS                 = "camino", 
  ARISTAS_POSIBLES_CAMINOS        = "posibles_camino", 
  ARISTAS_PUERTOS                 = "puertos",
  RESULTADO_TIRADA                = "Resultado_Tirada",
  RECURSOS                        = "Recursos", 
  CARTAS                          = "Cartas",
  PUNTUACIONES                    = "Puntuacion",
  TURNO                           = "Turno", 
  GANADOR                         = "Ganador",
  PLAYER_1                        = "Player_1",
  PLAYER_2                        = "Player_2",
  PLAYER_3                        = "Player_3",
  PLAYER_4                        = "Player_4",
}

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
   * Actualiza los recursos del jugador dado, con la información en recursos.
   * 
   * @param recursos array de 5 componentes enteras tal que: 
   * [num_madera, num_piedra, num_ladrillo, num_lana, num_cereales]
   * @param playerIndex indice del jugador cuyos recursos se van a actualizar dentro
   * del vector de jugadores de la partida. 
   */
  private actualizarRecursosJugador(recursos: Array<number>, playerIndex: number){
    
    this.partida.jugadores[playerIndex].recursos.madera   = recursos[0]
    this.partida.jugadores[playerIndex].recursos.piedra   = recursos[1]
    this.partida.jugadores[playerIndex].recursos.ladrillo = recursos[2]
    this.partida.jugadores[playerIndex].recursos.lana     = recursos[3]
    this.partida.jugadores[playerIndex].recursos.cereales = recursos[4]
  }

  /**
   * Actualiza los recursos de los jugadores con los datos recibidos.
   * 
   * @param recursos campo "Recursos" del mensaje recibido del servidor. Contiene
   * información actualizada sobre los recursos de los que dispone cada jugador. 
   */
  private actualizarRecursosJugadores(recursos: Object){

    if ( recursos[MessageKeys.PLAYER_1] ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_1], 0)
    } else {
      console.log("Faltan los recursos del jugador 1")
    }

    if ( recursos[MessageKeys.PLAYER_2] ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_2], 1)
    } else {
      console.log("Faltan los recursos del jugador 2")
    }

    if ( recursos[MessageKeys.PLAYER_3] ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_3], 2)
    } else {
      console.log("Faltan los recursos del jugador 3")
    }

    if ( recursos[MessageKeys.PLAYER_4] ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_4], 3)
    } else {
      console.log("Faltan los recursos del jugador 4")
    }

  }


  /**
   * Actualiza las cartas del jugador dado, con la información en cartas.
   * 
   * @param recursos array de 7 componentes enteras tal que: 
   * [num_D1, num_D2, num_D3, num_D4, num_D5, num_E1, num_E2]
   * @param playerIndex indice del jugador cuyas cartas se van a actualizar dentro
   * del vector de jugadores de la partida. 
   */
  private actualizarCartasJugador(cartas: Array<number>, playerIndex: number){
    
    this.partida.jugadores[playerIndex].cartas.D1 = cartas[0]
    this.partida.jugadores[playerIndex].cartas.D2 = cartas[1]
    this.partida.jugadores[playerIndex].cartas.D3 = cartas[2]
    this.partida.jugadores[playerIndex].cartas.D4 = cartas[3]
    this.partida.jugadores[playerIndex].cartas.D5 = cartas[4]
    this.partida.jugadores[playerIndex].cartas.E1 = cartas[5]
    this.partida.jugadores[playerIndex].cartas.E2 = cartas[6]
  }

  
  /**
   * Actualiza la información de las cartas de los jugadores con los datos
   * recibidos.
   * 
   * @param cartas campo "Cartas" del mensaje recibido del servidor. Contiene
   * información actualizada sobre las cartas de las que dispone cada jugador. 
   */
  private actualizarCartasJugadores(cartas: Object){

    if ( cartas[MessageKeys.PLAYER_1] ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_1], 0)
    } else {
      console.log("Faltan las cartas del jugador 1")
    }

    if ( cartas[MessageKeys.PLAYER_2] ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_2], 1)
    } else {
      console.log("Faltan las cartas del jugador 2")
    }

    if ( cartas[MessageKeys.PLAYER_3] ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_3], 2)
    } else {
      console.log("Faltan las cartas del jugador 3")
    }

    if ( cartas[MessageKeys.PLAYER_4] ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_4], 3)
    } else {
      console.log("Faltan las cartas del jugador 4")
    }
  }


  /**
   * Actualiza la puntuación de los jugadores con los nuevos datos
   * recibidos.
   * 
   * @param puntuacion campo "Puntuacion" del mensaje recibido del 
   * servidor. Contiene las puntuaciones actualizadas de cada uno de
   * los jugadores
   */
  private actualizarPuntuacionJugadores(puntuacion: Object){
    
    if ( puntuacion[MessageKeys.PLAYER_1] ){
      this.partida.jugadores[0].puntos = puntuacion[MessageKeys.PLAYER_1]
    }else{
      console.log("Falta la puntuación del jugador 1")
    }

    if ( puntuacion[MessageKeys.PLAYER_2] ){
      this.partida.jugadores[1].puntos = puntuacion[MessageKeys.PLAYER_2]
    }else{
      console.log("Falta la puntuación del jugador 2")
    }

    if ( puntuacion[MessageKeys.PLAYER_3] ){
      this.partida.jugadores[2].puntos = puntuacion[MessageKeys.PLAYER_3]
    }else{
      console.log("Falta la puntuación del jugador 1")
    }

    if ( puntuacion[MessageKeys.PLAYER_4] ){
      this.partida.jugadores[3].puntos = puntuacion[MessageKeys.PLAYER_4]
    }else{
      console.log("Falta la puntuación del jugador 1")
    }
  }


  /**
   * Actualiza la información de los jugadores con los nuevos datos
   * recibidos
   * 
   * @param msg mensaje recibido con la nueva información de la partida
   * (incluye recursos, cartas y puntos de los jugadores)
   */
  private actualizarJugadores(msg: Object): void{

    if ( msg[MessageKeys.RECURSOS] ){
      this.actualizarRecursosJugadores(msg[MessageKeys.RECURSOS])
    }else{
      console.log("Faltan los recursos en el mensaje")
    }

    if ( msg[MessageKeys.CARTAS] ){
      this.actualizarCartasJugadores(msg[MessageKeys.CARTAS])
    }else{
      console.log("Faltan las cartas en el mensaje")
    }

    if ( msg[MessageKeys.PUNTUACIONES] ){
      this.actualizarPuntuacionJugadores(msg[MessageKeys.PUNTUACIONES])
    }else{
      console.log("Faltan las puntuaciones en el mensaje")
    }

  }


  /**
   * Actualiza la información de los terrenos del tablero
   * 
   * @param hexagonos campo "hexagono" del campo "Tab_info" del mensaje recibido 
   * del servidor. Contiene la información actualizada de los hexágonos (terrenos)
   * de la partida.
   */
  private actualizarHexagonos(hexagonos: Object): void {

    if ( hexagonos[MessageKeys.HEXAGONOS_TIPOS] ){
      this.partida.tablero.hexagonos.tipo = hexagonos[MessageKeys.HEXAGONOS_TIPOS]
    }else{
      console.log("Faltan los tipos de los hexágonos")
    }

    if ( hexagonos[MessageKeys.HEXAGONOS_VALORES] ){
      this.partida.tablero.hexagonos.valor = hexagonos[MessageKeys.HEXAGONOS_VALORES]
    }else{
      console.log("Faltan los valores de los hexágonos")
    }

    if ( hexagonos[MessageKeys.HEXAGONOS_LADRON] ){
      this.partida.tablero.hexagonos.ladron = hexagonos[MessageKeys.HEXAGONOS_LADRON]
    }else{
      console.log("Faltan el ladrón")
    }
  }


  /**
   * Actualiza la información de los asentamientos del tablero
   * 
   * @param Vertices campo "vertices" del campo "Tab_info" del mensaje
   * recibido del servidor. COntiene la información actualizada de los vértices
   * (asentamientos) del tablero. 
   */
  private actualizarVertices(Vertices: Object): void {

    if ( Vertices[MessageKeys.VERTICES_ASENTAMIENTOS] ){
      this.partida.tablero.vertices.asentamiento = Vertices[MessageKeys.VERTICES_ASENTAMIENTOS]
    }else{
      console.log("Falta la información sobre asentamientos")
    }

    if ( Vertices[MessageKeys.VERTICES_POSIBLES_ASENTAMIENTOS] ){
      let posiblesAsentamientos: Array<String> = Vertices[MessageKeys.VERTICES_POSIBLES_ASENTAMIENTOS]
      for (let i = 0; i < posiblesAsentamientos.length; i++){
        this.partida.tablero.vertices.posible_asentamiento[i] = posiblesAsentamientos[i] == 'true' 
      }
    }else{
      console.log("Falta la información sobre posibles asentamientos")
    }
  }


  /**
   * Actualiza la información de los caminos del tablero.
   * 
   * @param Aristas campo "aristas" del campo "Tab_info" del mensaje
   * recibido del servidor. COntiene la información actualizada de las aristas
   * (caminos) del tablero. 
   */
  private actualizarAristas(Aristas: Object): void {

    if ( Aristas[MessageKeys.ARISTAS_CAMINOS] ){
      this.partida.tablero.aristas.camino = Aristas[MessageKeys.ARISTAS_CAMINOS]
    }else{
      console.log("Falta la información sobre los caminos")
    }

    if ( Aristas[MessageKeys.ARISTAS_POSIBLES_CAMINOS] ){
      let posiblesCaminos: Array<String> = Aristas[MessageKeys.ARISTAS_POSIBLES_CAMINOS]
      for (let i = 0; i < posiblesCaminos.length; i++){
        this.partida.tablero.aristas.posible_camino[i] = posiblesCaminos[i] == 'true' 
      }
    }else{
      console.log("Falta la información sobre posibles caminos")
    }

    if ( Aristas[MessageKeys.ARISTAS_PUERTOS] ){
      this.partida.tablero.aristas.puerto = Aristas[MessageKeys.ARISTAS_PUERTOS]
    }else{
      console.log("Falta información sobre los puertos")
    }
  }


  /**
   * Actualiza el tablero con la nueva información recibida
   * 
   * @param tab_info mensaje que contiene los campos "hexagono", "vertices" y "aristas". 
   * Debería de ser el contenido de "Tab_info" del mensaje recibido desde el servidor.
   */
  private actualizarTablero(tab_info: Object): void {
    if ( tab_info[MessageKeys.TAB_INFO_HEXAGONOS] ){
      this.actualizarHexagonos(tab_info[MessageKeys.TAB_INFO_HEXAGONOS])
    }else{
      console.log("Faltan los hexágonos en el mensaje")
    }

    if ( tab_info[MessageKeys.TAB_INFO_VERTICES]){
      this.actualizarVertices(tab_info[MessageKeys.TAB_INFO_VERTICES])
    }else{
      console.log("Faltan los vértices en el mensaje")
    }

    if ( tab_info[MessageKeys.TAB_INFO_ARISTAS] ){
      this.actualizarAristas(tab_info[MessageKeys.TAB_INFO_ARISTAS])
    }else{
      console.log("Faltan las aristas en el mensaje")
    }
  }


  /**
   * Actualiza la información de la partida conforme a los nuevos
   * datos recibidos
   * 
   * @param msg mensaje con la nueva información de la partida
   */
  private actualizarPartida(msg: Object): void {
    this.partida.turnoActual = msg[MessageKeys.TURNO]
    this.partida.resultadoTirada = msg[MessageKeys.RESULTADO_TIRADA]
    this.actualizarJugadores(msg)
    this.actualizarTablero(msg[MessageKeys.TAB_INFO])
  }


  /**
   * Procesa un mensaje de partida (con información del tablero)
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajePartida(msg: Object): void{
    if (msg[MessageKeys.EXIT_STATUS] == 0) {

      //Actualizar la partida con la nueva información
      this.actualizarPartida(msg)

      if (msg[MessageKeys.GANADOR]) {
        //Finalizar la partida y mostrar estadísticas
        this.finalizarpartida()
      }
    }
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
