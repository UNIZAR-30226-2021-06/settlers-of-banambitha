import { Component, Inject, Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Connectable, WsService } from '../ws/ws.service';
import { UserService } from '../user/user.service';
import { RoomService, UserCardInfo } from '../room/room.service';
import { MatSnackBar, MatSnackBarRef, MAT_SNACK_BAR_DATA } from '@angular/material/snack-bar';
import { LangService } from '../lang/lang.service';


/**
 * Algunos de los identificadores del mensaje de respuesta
 * del backend son un poco raros, así que esta clase
 * formaliza los identificadores y facilita cambiarlos
 * si es que se cambian desde backend en algún momento.
 * Ej: posibles_camino????
 */
enum MessageKeys {
  MENSAJE                         = "Message",
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
  TURNO_ACUM                      = "Turno", 
  TURNO_JUGADOR                   = "Turno_Jugador", 
  GANADOR                         = "Ganador",
  PLAYER_1                        = "Player_1",
  PLAYER_2                        = "Player_2",
  PLAYER_3                        = "Player_3",
  PLAYER_4                        = "Player_4",
  PUERTO_MADERA                   = "puertoMadera",
  PUERTO_LANA                     = "puertoLana",
  PUERTO_MINERAL                  = "puertoMineral",
  PUERTO_CEREAL                   = "puertoCereales",
  PUERTO_ARCILLA                  = "puertoArcilla",
  PUERTOS_BASICOS                 = "puertosBasicos",
  CLOCK                           = "Clock",
  PRIMEROS_CAMINOS                = "primerosCaminos",
  PRIMEROS_ASENTAMIENTOS          = "primerosAsentamiento",
  PLAYER_NAMES                    = "playerNames"
}


/**
 * Estados posibles de un mensaje de comercio
 */
enum MsgComercioStatus {
  REQUEST = "REQUEST",
  ACCEPT  = "ACCEPT",
  DECLINE = "DECLINE"
}

/**
 * Estados posibles de una mensaje de reporte
 */
enum MsgReporteStatus {
  REPORT_SENT     = "REPORT_SENT", 
  REPORT_REJECTED = "REPORT_REJECTED",
  REPORT_RECEIVED = "REPORT_RECEIVED"
}


/**
 * Jugadas posibles
 */
enum Jugada {
  CONSTRUIR_POBLADO   = "construir poblado",
  MEJORAR_POBLADO     = "mejorar poblado",
  CONSTRUIR_CAMINO    = "construir camino", 
  MOVER_LADRON        = "mover ladron", 
  PASAR_TURNO         = "finalizar turno",
  PRIMER_ASENTAMIENTO = "primer asentamiento", 
  PRIMER_CAMINO       = "primer camino",
  COMERCIAR           = "comerciar",
  COMERCIAR_PUERTO    = "comerciar con puerto"
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
 * Tipos de recursos
 */
export enum Recurso {
  MADERA  = "madera", 
  MINERAL = "mineral", 
  LANA    = "lana", 
  CEREAL  = "cereales", 
  ARCILLA = "arcilla"
}


/**
 * Tipos de puertos
 */
export enum TipoPuerto {
  MADERA, 
  MINERAL, 
  LANA, 
  ARCILLA, 
  CEREAL, 
  BASICO,
}


/**
 * Tipos de asentamientos (valores para un 
 * posible asentamiento del tablero)
 */
export enum TipoAsentamiento {
  NADA             = "Nada", 
  POBLADO_PLAYER_1 = "PuebloAzul", 
  POBLADO_PLAYER_2 = "PuebloRojo", 
  POBLADO_PLAYER_3 = "PuebloAmarillo", 
  POBLADO_PLAYER_4 = "PuebloVerde", 
  CIUDAD_PLAYER_1  = "CiudadAzul", 
  CIUDAD_PLAYER_2  = "CiudadRojo", 
  CIUDAD_PLAYER_3  = "CiudadAmarillo", 
  CIUDAD_PLAYER_4  = "CiudadVerde", 
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
  PLAYER_1 = "CaminoAzul", 
  PLAYER_2 = "CaminoRojo", 
  PLAYER_3 = "CaminoAmarillo",
  PLAYER_4 = "CaminoVerde"
}


/**
 * Mensaje de solicitud de jugada
 */
interface MsgJugada {
  player: number, 
  game: String, 
  move: {
    name: Jugada,
    param: Object
  }
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
  mineral:   number, 
  arcilla: number, 
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
  recursos: RecursosJugador,
  primerosAsentamientos: boolean,
  primerosCaminos:       boolean
}


/**
 * Mensaje que se puede recibir durante una partida
 */
export interface Mensaje {
  esError: boolean
  remitente: Jugador, 
  body:      String, 
  timeStamp: String
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
  posible_asentamiento: Array<Array<Boolean>>
}


/**
 * Puertos del tablero
 */
export interface Puertos {
  madera:   number, 
  mineral:   number, 
  arcilla: number, 
  lana:     number, 
  cereal:   number
  basico:   Array<Number>
}


/**
 * Información sobre TODAS las aristas
 * de un tablero
 */
export interface Aristas {
  camino:         Array<TipoCamino>, 
  posible_camino: Array<Array<Boolean>>,
  puertos:        Puertos
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
 * Interfaz auxiliar para gestionar una petición 
 * de comercio
 */
export interface ProductoComercio {
  type: Recurso, 
  cuan: number
}


/**
 * Solicitud de comercio
 */
export interface SolicitudComercio {
  from: number,
  res1: ProductoComercio, 
  res2: ProductoComercio,
  timeStamp: string
}


/**
 * Almacena toda la información necesaria de una partida
 */
export interface Partida {
  id:                    String,
  miTurno:               number,
  turnoActual:           number,
  totalTurnos:           number,
  tablero:               Tablero,
  resultadoTirada:       number, 
  jugadores:             Array<Jugador>,
  mensajes:              Array<Mensaje>, 
  clock:                 number,
  PobladoDisponible:     boolean,
  CaminoDisponible:      boolean, 
  CiudadDisponible:      boolean, 
  movioLadron:           boolean,
  yaComercio:            boolean,
  reported:              Array<Boolean>
}


@Injectable({
  providedIn: 'root'
})
/**
 * Clase que gestiona la interacción del usuario durante una partida
 */
export class GameService implements Connectable{

  private static readonly coloresPorId: Array<Color> = [Color.AZUL, Color.ROJO, Color.AMARILLO, Color.VERDE]
  private static readonly numAristas:   number = 72
  private static readonly numVertices:  number = 54
  private static readonly numHexagonos: number = 19
  private static readonly numJugadores: number = 4 //si se cambia este valor se tiene que cambiar buena parte del código

  public partida: Partida = {
      miTurno: 0,
      id: "",
      jugadores: this.inicializarJugadores(["","","",""]),
      turnoActual: 0, 
      totalTurnos: 0,
      tablero: this.tableroVacio(), 
      resultadoTirada: 0, 
      mensajes: [],
      clock: -1,
      PobladoDisponible: false,
      CiudadDisponible: false, 
      CaminoDisponible: false,
      movioLadron: false,
      yaComercio: false,
      reported: [false, false, false, false]
  }

  public cargandoPartida: boolean = false
  public ultimaSolicitudComercio: SolicitudComercio

  private roomService: RoomService; 

  //Identificadores de los topics a los que se suscribe 
  //el jugador
  private test_partida_topic_id:   any
  private partida_act_topic_id:    any
  private partida_chat_topic_id:   any
  private partida_com_topic_id:    any
  private partida_reload_topic_id: any
  private usuario_act_topic_id:    any

  //Cliente de stomp
  private stompClient: any

  /**
   * Constructor. Se suscribe a los topics necesarios para poder gestionar
   * toda la dinámica juego.
   * @param wsService   servicio de websockets a utilzar (singleton)
   * @param router      router de la aplicación
   * @param userService servicio de usuario a utilizar (singleton)
   */
  constructor(private wsService: WsService, private router: Router, private userService: UserService, private _snackBar: MatSnackBar) {

    if ( ! wsService.atatchConnectable(this)){
      this.onConnect();
    }

    this.cargandoPartida = false
    this.initData()

    // this.initPartidaPrueba()
    // this.ultimaSolicitudComercio = {
    //   from: 1, 
    //   res1: {
    //     type: Recurso.MADERA,
    //     cuan: 10
    //   },
    //   res2: {
    //     type: Recurso.LANA,
    //     cuan: 5
    //   },
    //   timeStamp: "12:12"
    // }
    // this.openWinnerSnackBar(2)

  }

  /**
   * Inicializa los datos de la partida (se asegura de que los valores
   * no son nulos)
   */
  public initData(): void {
    this.partida = {
      miTurno: 0,
      id: "",
      jugadores: this.inicializarJugadores(["","","",""]),
      turnoActual: 0, 
      totalTurnos: 0,
      tablero: this.tableroVacio(), 
      resultadoTirada: 0, 
      mensajes: [],
      clock: -1,
      PobladoDisponible: false,
      CiudadDisponible: false, 
      CaminoDisponible: false,
      movioLadron: false,
      yaComercio: false,
      reported: [false, false, ,false, false]
    }
  }

  /**
   * Abre una snack bar con la informacion de la ultima solicitud 
   * de comercio recibida
   */
  public openTradeOfferSnackBar(): void {
    this._snackBar.openFromComponent(TradeOfferSnackBar, {
      data: { gameService: this, solicitud: this.ultimaSolicitudComercio },
      horizontalPosition: "left",
      verticalPosition: "top"
    });
  }


  /**
   * Abre una snackbar con información sobre el ganador de la partida
   * 
   * @param winner id del jugador que ganó la partida
   */
  public openWinnerSnackBar(winner: number): void {
    let winnerName: String = (winner == this.partida.miTurno) ? "Has" : (this.partida.jugadores[winner - 1].nombre + " ha")
    this._snackBar.open("¡" + winnerName + " ganado la partida!", "Entendido") 
  }


  /**
   * Función a llamar cuando la conexión websocket con el 
   * servidor se haya establecido
   */
  public onConnect(): void {

    this.stompClient = this.wsService.getStompClient() 
  }


  /**
   * Envía una solicitud para recargar la partida
   * 
   * @param idPartida id de la partida a recargar
   */
  public recargarPartida(idPartida: string): void {
    
    this.finalizarpartida()

    this.partida.id = idPartida

    let msg = {
      player: this.userService.getUsername(),
      game: idPartida,
      reload: true
    }

    let that = this
    //Topic de recargar parrida
    this.partida_com_topic_id = this.stompClient.subscribe(WsService.partida_reload_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        that.procesarMensajeRecarga(JSON.parse(message.body))
      }else{
        console.log("Error crítico")
      }
    });

    this.stompClient.send(WsService.partidaRecargar, {}, JSON.stringify(msg) )

    this.cargandoPartida = true
  }


  /**
   * Inicia una partida de prueba, donde el único jugador
   * es el usuario loggeado.
   * Solo útil para test
   * 
   * @param simulateMoves ordenar al servidor simular jugadas en 
   * la partida de prueba
   */
  public comenzarPartidaPrueba( simulateMoves: boolean, roomService: RoomService){

    let that = this
    this.test_partida_topic_id = this.stompClient.subscribe(WsService.partida_test_topic +
      this.userService.getUsername(), function (message) {
      if (message.body){
        let msg: Object = JSON.parse(message.body)
        console.log(msg)
        console.log("Comenzando partida de prueba...")
        that.comenzarPartida(msg["game"],
                            [that.userService.getUsername(),
                            "Some1" ,"Some2", "Some3"], roomService)

      }else{
        console.log("Error crítico")
      }
    });
    this.router.navigate(["/board"])
    //Solicitar inicio de partida
    this.stompClient.send(WsService.partidaTestComenzar, {},
      JSON.stringify({from: this.userService.getUsername(), simulate: simulateMoves}) )
  }


  /**
   * Se conecta a una partica (puede ser una partida ya en curso)
   * 
   * @param idPartida identificador de la partida
   * @param jugadores jugadores participantes (deben ser 4)
   * @Return true si ha podido inicializar la partida, false en 
   * caso contrario
   */
  public comenzarPartida(idPartida: String, jugadores: Array<String>, roomService: RoomService): boolean{
    
    this.roomService = roomService

    console.log("Iniciando partida...")
    console.log(jugadores)
    this.cargandoPartida = true
    let miTurno: number = jugadores.indexOf(this.userService.getUsername())
    if ( jugadores.length == GameService.numJugadores && miTurno >= 0){
      console.log("comenzando partida")
      this.partida.miTurno = miTurno + 1
      this.partida.id = idPartida
      this.partida.jugadores = this.inicializarJugadores(jugadores)
      this.partida.clock = -1

      this.subscribeToTopics()
      this.router.navigate(["/board"])
      return true
    }
    console.log("no se pudo comenzar la partida")

    return false
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
      this.partida_com_topic_id = this.stompClient.subscribe(WsService.partida_com_topic + this.partida.id + "/" + this.partida.miTurno,
      function (message) {
        if (message.body){
          that.procesarMensajeComercio(JSON.parse(message.body))
        }else{
          console.log("Error crítico")
        }
      });

      //Suscripción a las respuestas de reporte
      this.usuario_act_topic_id = this.stompClient.subscribe(WsService.usuario_act + this.userService.getUsername(),
      function (message) {
        if (message.body){
          that.procesarMensajeReporte(JSON.parse(message.body))
        }else{
          console.log("Error crítico")
        }
      });

    }
  }

  /**
   * Procesa un mensaje de respuesta a un reporte. 
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajeReporte(msg: Object){
    console.log(msg)
    switch(msg["status"]){

      case MsgReporteStatus.REPORT_RECEIVED: 
        this.generarMensajePartida("¡" + msg["player"] + " te ha reportado!" )
        break;

      case MsgReporteStatus.REPORT_REJECTED: 
        this.generarMensajeErrorPartida("¡No puedes reportar a  " + msg["player"] + " más de una vez por partida!" )
        break;

      case MsgReporteStatus.REPORT_SENT:
        this.generarMensajePartida("¡Has reportado a " + msg["player"] + "!" )
        break;

      default: 
        console.log("Estado desconocido")
        break; 

    }
  }

  /**
   * Procesa un mensaje de partida (con información del tablero)
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajePartida(msg: Object): void{
      //Actualizar la partida con la nueva información
      this.actualizarPartida(msg)
      this.cargandoPartida = false

      if (msg[MessageKeys.GANADOR] > 0) {
        //Finalizar la partida y mostrar estadísticas
        this.roomService.crearSala()
        this.router.navigate(["/home/profile"])
        this.openWinnerSnackBar(msg[MessageKeys.GANADOR])
        this.finalizarpartida()
      }
  }


  /**
   * Procesa el mensaje de recarga de partida
   * 
   * @param msg 
   */
  public procesarMensajeRecarga(msg: Object): void{

    console.log("Ahora la partida es: " + this.partida.id)

    let playerNames: Array<String> = msg[MessageKeys.PLAYER_NAMES]
    let miTurno: number = playerNames.indexOf(this.userService.getUsername())
    if (  miTurno >= 0 ){
      console.log("comenzando partida")
      this.partida.miTurno = miTurno + 1
      this.partida.clock = -1

      this.procesarMensajePartida(msg)
      console.log("la partida: ")
      console.log(this.partida.id)
      this.subscribeToTopics()

    }else{

      console.log("no estabas en esa partida!")
    }
  }


  /**
   * Procesa un mensaje del chat de la partida
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajeChat(msg: Object): void{
    console.log(msg)
    let mensajeNuevo: Mensaje = {
      remitente: this.partida.jugadores[msg["from"] - 1], 
      body: msg["body"],
      timeStamp: msg["time"],
      esError: false
    }

    //TODO: ordenar los mensajes por timestamp, habrá que 
    //cambiar el tipo de dato de simeStamp
    this.partida.mensajes.push(mensajeNuevo)
  }


  /**
   * Procesa un mensaje de comercio
   * 
   * @param msg 
   */
  private procesarMensajeComercio(msg: Object): void{
    console.log(msg)
    let infoMsg: string
    switch (msg["type"]){

      case MsgComercioStatus.REQUEST: 
        if ( this.partida.turnoActual != this.partida.miTurno ){
          this.ultimaSolicitudComercio = {
            from: msg["from"],
            res1: msg["res1"],
            res2: msg["res2"],
            timeStamp: msg["time"]
          }
          this.openTradeOfferSnackBar()
          infoMsg = "¡" + this.partida.jugadores[msg["from"] - 1].nombre + " quiere comerciar contigo!"
          this.generarMensajePartida(infoMsg)
        }
        break

      case MsgComercioStatus.ACCEPT: 
        infoMsg = "¡" + this.partida.jugadores[msg["from"] - 1].nombre + " ha ACEPTADO tu solicitud de comercio!"
        this.generarMensajePartida(infoMsg)
        break

      case MsgComercioStatus.DECLINE: 
        infoMsg = "¡" + this.partida.jugadores[msg["from"] - 1].nombre + " ha RECHAZADO tu solicitud de comercio!"
        this.generarMensajePartida(infoMsg)
        break

      default: 
    }
  }


  /**
   * Finaliza la partida actual (solo en el lado del cliente)
   */
  private finalizarpartida() {

    this.cargandoPartida = false

    this.partida = {
      miTurno: 0,
      id: "",
      jugadores: this.inicializarJugadores(["","","",""]),
      turnoActual: 0, 
      totalTurnos: 0,
      tablero: this.tableroVacio(), 
      resultadoTirada: 0, 
      mensajes: [],
      clock: -1,
      PobladoDisponible: false,
      CiudadDisponible: false, 
      CaminoDisponible: false,
      movioLadron: false,
      yaComercio: false ,
      reported: [false, false, false, false]
    }

    if ( this.partida_act_topic_id != null ){
      this.partida_act_topic_id.unsubscribe()
    }

    if ( this.partida_com_topic_id != null ){
      this.partida_com_topic_id.unsubscribe()
    }

    if ( this.partida_chat_topic_id != null ){
      this.partida_chat_topic_id.unsubscribe()
    }

    if ( this.partida_reload_topic_id != null ){
      this.partida_reload_topic_id.unsubscribe()
    }

    if ( this.usuario_act_topic_id != null ){
      this.usuario_act_topic_id.unsubscribe()
    }
  }


  /****************************************************************
  *                     ACCIONES POSIBLES
  *****************************************************************/


  /**
   * Crea un poblado en el vértice con el identificador dado. 
   * Solo creará el poblado si el vértice está vacío y el usuario 
   * dispone de los recursos necesarios.
   * 
   * @param vertice arista del vértice en el que se construirá el
   * poblado
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */ 
  public construirPoblado( vertice: number ): boolean {

    if ( this.esMiTurno() &&
         this.verticeValido(vertice) &&
         this.partida.tablero.vertices.asentamiento[vertice] == TipoAsentamiento.NADA && 
         this.puedeConstruirPoblado() ){

      let msg = this.construirJugada(Jugada.CONSTRUIR_POBLADO, vertice)

      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Crea un camino en la arista con el identificador dado. 
   * Solo creará el camino si la arista está vacía y el usuario 
   * dispone de los recursos necesarios. 
   * 
   * @param arista arista en la que se construirá el camino
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public construirCamino( arista: number): boolean {

    if ( this.esMiTurno() &&
         this.aristaValida(arista) &&
         this.partida.tablero.aristas.camino[arista] == TipoCamino.NADA && 
         this.puedeConstruirCamino() ) {

      let msg = this.construirJugada(Jugada.CONSTRUIR_CAMINO, arista)

      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Crea el primer poblado en el vértice dado. Solo 
   * creará el poblado si el vértice está vacío.
   * 
   * @param vertice vértice en el que se construirá el poblado
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public construirPrimerPoblado( vertice: number): boolean {

    if ( this.esMiTurno() &&
         this.verticeValido(vertice) &&
         this.partida.tablero.vertices.asentamiento[vertice] == TipoAsentamiento.NADA){

      let msg = this.construirJugada(Jugada.PRIMER_ASENTAMIENTO, vertice)
      
      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Crea el primer camino en la arista con el identificador dado. 
   * Solo creará el camino si la arista está vacía. 
   * 
   * @param arista arista en la que se construirá el camino
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public construirPrimerCamino( arista: number ): boolean {
    
    if ( this.esMiTurno() &&
         this.aristaValida(arista) &&
         this.partida.tablero.aristas.camino[arista] == TipoCamino.NADA ) {

      let msg = this.construirJugada(Jugada.PRIMER_CAMINO, arista)
      
      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false

  }


  /**
   * Mejora el pueblo dado a una ciudad. Solo se puede realizar
   * esta acción si es el turno del usuario, dispone de los
   * recursos suficientes como para mejorar el pueblo y existe un pueblo
   * del usuario en el vértice dado
   * 
   * @param vertice vertice del pueblo a mejorar
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public mejorarPueblo(vertice: number): boolean {

    if ( this.esMiTurno() &&
         this.verticeValido(vertice) && 
         this.partida.tablero.vertices.asentamiento[vertice] == this.miTipoPueblo() &&
         this.puedeConstruirPoblado() ){ 
      
      let msg = this.construirJugada(Jugada.MEJORAR_POBLADO, vertice)

      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Mueve el ladrón al hexágono dado. Solo se podrá mover al 
   * ladrón si es el turno del usuario y el resultado de la
   * tirada es un 7.
   * 
   * @param hexagono hexágono al que se moverá el ladrón
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public moverLadron( hexagono: number): boolean {

    if ( this.esMiTurno() &&
         this.partida.resultadoTirada == 7  
         && !this.partida.movioLadron){

      this.partida.movioLadron = true
      let msg = this.construirJugada(Jugada.MOVER_LADRON, hexagono)

      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Finaliza el turno del usuario. Solo tiene efecto si el 
   * turno actual es efectivamente el turno del usuario.
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public pasarTurno(): boolean {

    if ( this.esMiTurno() ){
      let msg = this.construirJugada(Jugada.PASAR_TURNO, "")

      this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Envía una petición de comercio al jugador dado, solicitando el 
   * intercambio de recursos detallado.
   * Solo se enviará la petición si el turno actual es el turno del
   * usuario y además las cantidades a intercambiar son factibles (los
   * usuarios disponen de recursos suficientes para realizar el intercambio)
   * 
   * @param jugador jugador al que se solicita el intercambio
   * @param recursoOfrecido tipo de recurso ofrecido
   * @param recursoSolicitado tipo de recurso solicitado
   * @param cantidadOfrecida cantidad de recursos ofrecida
   * @param cantidadSolicitada cantidad de recursos solicitada
   * @return true si ha podido enviar la petición, false en caso 
   * contrario
   */
  public comerciarConJugador(jugador: number, recursoOfrecido: Recurso, recursoSolicitado: Recurso, 
                            cantidadOfrecida: Number, cantidadSolicitada: Number): void {
    console.log("Proponer comercio a " + jugador)
    let msg = {
      from: this.partida.miTurno, 
      to:   jugador,
      game: this.partida.id,
      res1: {
        type: recursoOfrecido,
        cuan: cantidadOfrecida
      },
      res2: {
        type: recursoSolicitado, 
        cuan: cantidadSolicitada
      }
    }
    this.stompClient.send(WsService.proponerComercio, {}, JSON.stringify(msg) )
  }


  /**
   * Realiza un intercambio con el puerto dado por aristaPuerto. Solo se efectuará el 
   * intercambio si el jugador dispone de los recursos suficientes. Y el puerto es del 
   * tipo correcto (ofrece el recurso solicitado y acepta el recurso ofrecido)
   * 
   * @param aristaPuerto puerto con el que se realizará el intercambio
   * @param recursoSolicitado tipo de recurso solicitado
   */
  public comerciarConPuerto(aristaPuerto: number, recursoSolicitado: Recurso, 
                            numMadera: number, numArcilla: number, numMineral: number, 
                            numCereales: number, numLana: number): void {
    let solicitud: Object = {
      id_puerto: aristaPuerto, 
      materiales: {
        madera:   numMadera,
        lana:     numLana, 
        cereales: numCereales,
        arcilla:  numArcilla, 
        mineral:  numMineral 
      },
      material_que_recibe: recursoSolicitado
    }
    let msg = this.construirJugada(Jugada.COMERCIAR_PUERTO, solicitud)
    this.stompClient.send(WsService.partidaJugada, {}, JSON.stringify(msg) )
  }

                    
  /**
   * Acepta la última petición de comercio recibida
   * Solo se pueden aceptar peticiones fuera del turno del jugador
   * 
   */
  public aceptarComercioJugador(): void {
    if ( this.partida.turnoActual != this.partida.miTurno ){
      let msg = {
        from: this.partida.miTurno, 
        to:   this.ultimaSolicitudComercio.from,
        game: this.partida.id,
        res1: {
          type: this.ultimaSolicitudComercio.res1.type,
          cuan: this.ultimaSolicitudComercio.res1.cuan
        },
        res2: {
          type: this.ultimaSolicitudComercio.res2.type,
          cuan: this.ultimaSolicitudComercio.res2.cuan
        }
      }
      this.stompClient.send(WsService.aceptarComercio, {}, JSON.stringify(msg) )
    }
  }              


  /**
   * Rechaza la última petición de comercio
   * Solo se pueden rechazar peticiones fuera del turno de partida
   * 
   */
  public rechazarComercioJugador(): void {
    let msg = {
      from: this.partida.miTurno, 
      to:   this.ultimaSolicitudComercio.from,
      game: this.partida.id
    }
    this.stompClient.send(WsService.rechazarComercio, {}, JSON.stringify(msg) )
  }


  /**
   * Envía un mensaje al chat de la partida
   * 
   * @param mensaje cuerpo del mensaje
   */
  public enviarMensaje(mensaje: String): void{
    let msg = {
      from: this.partida.miTurno,
      game: this.partida.id, 
      body: mensaje
    }
    this.stompClient.send(WsService.enviarMensajePartida, {}, JSON.stringify(msg) )
  }


  /**
   * Reporta al jugador dado. Solo se puede reportar una vez
   * por partida. 
   * 
   * @param playerId jugador que se desea reportar
   */
  public reportarJugador(playerId: number): void {
    if ( !this.partida.reported[playerId - 1] ){
      let msg = {
        from: this.userService.getUsername(),
        to: this.partida.jugadores[playerId - 1].nombre
      }
      this.stompClient.send(WsService.usuarioReportar, {}, JSON.stringify(msg) )
      this.partida.reported[playerId - 1] = true
    }
  }


  /****************************************************************
  * FUNCIONES AUXILIARES PARA TRADUCIR EL MENSAJE DEL
  * TABLERO A LAS ESTRUCTURAS DE DATOS INTERNAS E INICIALIZARLAS
  *****************************************************************/


  /**
   * Actualiza los atributos precalculados de la clase
   */
  private actualizarPrecalculos(): void {
    this.partida.CaminoDisponible  = this.puedeConstruirCamino()
    this.partida.CiudadDisponible  = this.puedeConstruirCiudad()
    this.partida.PobladoDisponible = this.puedeConstruirPoblado()
    if ( !this.esMiTurno() ){
      this.partida.movioLadron = false
      this.partida.yaComercio  = false
    }
  }


  /**
   * 
   * @return el número de poblados construidos por el jugador
   */
  private contarMisPoblados(): number{
    let n: number = 0
    let tipoAsentamiento: TipoAsentamiento = this.miTipoPueblo()
    for (let i = 0; i < this.partida.tablero.vertices.asentamiento.length; i++){
      if ( this.partida.tablero.vertices.asentamiento[i] == tipoAsentamiento ){
        n++
      }
    }

    return n
  }


  /**
   * 
   * @return el número de caminos construidos por el jugador
   */
  private contarMisCaminos(): number{
    let n: number = 0
    let tipoCamino: TipoCamino = this.miTipoCamino()
    for (let i = 0; i < this.partida.tablero.aristas.camino.length; i++){
      if ( this.partida.tablero.aristas.camino[i] == tipoCamino ){
        n++
      }
    }

    return n
  }



  /**
   * Devuelve el tipo de puerto que se encuentra en una arista
   * Si la arista no tiene puerto devuelve null
   * 
   * @param Arista arista cuyo tipo de puerto se quiere comprobar
   * @return tipo de puerto de la arista o null si no tenía puerto 
   */
  public TipoPuerto(Arista: number): TipoPuerto {

    if ( this.partida.tablero.aristas.puertos.arcilla == Arista){
      return TipoPuerto.ARCILLA
    }

    if ( this.partida.tablero.aristas.puertos.madera == Arista){
      return TipoPuerto.MADERA
    }

    if ( this.partida.tablero.aristas.puertos.mineral == Arista){
      return TipoPuerto.MINERAL
    }

    if ( this.partida.tablero.aristas.puertos.lana == Arista){
      return TipoPuerto.LANA
    }

    if ( this.partida.tablero.aristas.puertos.cereal == Arista){
      return TipoPuerto.CEREAL
    }

    for ( let i = 0; i < this.partida.tablero.aristas.puertos.basico.length; i++){
      if (this.partida.tablero.aristas.puertos.basico[i] == Arista){
        return TipoPuerto.BASICO
      }
    }

    return null
  }


  /**
   * @return True si es el turno del usuario, false en caso 
   * contrario
   */
  private esMiTurno(): boolean{
    return this.partida.miTurno == this.partida.turnoActual
  }


  /**
   * @return true si el usuario tiene los recursos 
   * suficientes como para construir un poblado
   */
  public puedeConstruirPoblado(): boolean {
    let arcilla: number = this.partida.jugadores[this.partida.miTurno - 1].recursos.arcilla
    let madera:  number = this.partida.jugadores[this.partida.miTurno - 1].recursos.madera
    let lana:    number = this.partida.jugadores[this.partida.miTurno - 1].recursos.lana
    let cereal:  number = this.partida.jugadores[this.partida.miTurno - 1].recursos.cereales
    return (!this.partida.jugadores[this.partida.miTurno - 1].primerosAsentamientos) ||
           arcilla > 0 && cereal > 0 && lana > 0 && madera > 0
  }


  /**
   * @return true si el usuario tiene los recursos 
   * suficientes como para construir una ciudad
   */
  public puedeConstruirCiudad(): boolean {
    let cereal:   number = this.partida.jugadores[this.partida.miTurno - 1].recursos.cereales
    let mineral:  number = this.partida.jugadores[this.partida.miTurno - 1].recursos.mineral
    return mineral > 2 && cereal > 1
  }


  /**
   * @return true si el usuario tiene los recursos 
   * suficientes como para construir un camino
   */
  public puedeConstruirCamino(): boolean {
    let arcilla: number = this.partida.jugadores[this.partida.miTurno - 1].recursos.arcilla
    let madera:  number = this.partida.jugadores[this.partida.miTurno - 1].recursos.madera
    return (!this.partida.jugadores[this.partida.miTurno - 1].primerosCaminos) ||
            arcilla > 0 && madera > 0
  }


  /**
   * Deveuvle el tipo de pueblo correspondiente al 
   * jugador
   * 
   * @return Tipo de asentamiento correspondente a un pueblo
   * del jugador
   */
  public miTipoPueblo(): TipoAsentamiento {
    switch(this.partida.miTurno){
      case 1: 
        return TipoAsentamiento.POBLADO_PLAYER_1

      case 2: 
        return TipoAsentamiento.POBLADO_PLAYER_2

      case 3: 
        return TipoAsentamiento.POBLADO_PLAYER_3

      case 4: 
        return TipoAsentamiento.POBLADO_PLAYER_4

      default: 
        return TipoAsentamiento.NADA
    }
  }


  /**
   * Deveuvle el tipo de ciudad correspondiente al 
   * jugador
   * 
   * @return Tipo de asentamiento correspondente a una ciudad
   * del jugador
   */
  public miTipoCiudad(): TipoAsentamiento {
    switch(this.partida.miTurno){
      case 1: 
        return TipoAsentamiento.CIUDAD_PLAYER_1

      case 2: 
        return TipoAsentamiento.CIUDAD_PLAYER_2

      case 3: 
        return TipoAsentamiento.CIUDAD_PLAYER_3

      case 4: 
        return TipoAsentamiento.CIUDAD_PLAYER_4

      default: 
        return TipoAsentamiento.NADA
    }
  }


  /**
   * Deveuvle el tipo de camino correspondiente al 
   * jugador
   * 
   * @return Tipo de camino correspondente a una camino
   * del jugador
   */
  public miTipoCamino(): TipoCamino {
    switch(this.partida.miTurno){
      case 1: 
        return TipoCamino.PLAYER_1

      case 2: 
        return TipoCamino.PLAYER_2

      case 3: 
        return TipoCamino.PLAYER_3

      case 4: 
        return TipoCamino.PLAYER_4

      default: 
        return TipoCamino.NADA
    }
  }


  /**
   * General el mensaje de petición de una jugada
   * 
   * @param jugada 
   * @param param 
   * @return el objeto a enviar al servidor para solicitar la jugada 
   * deseada
   */
  private construirJugada(jugada: Jugada, param: Object): MsgJugada {
    return {
      player: this.partida.miTurno,
      game: this.partida.id, 
      move: {
        name: jugada, 
        param: param,
      }
    }
  }

  
  /**
   * Devuelve true si el identificador del vértice es válido
   * 
   * @param vertice identificador del vértice a comprobar
   * @return true si el identificador del vértice es válido, 
   * false en caso contrario
   */
  private verticeValido(vertice: number): boolean{
    return vertice >= 0 && vertice < GameService.numVertices
  }


  /**
   * Devuelve true si el identificador de la arista es válido
   * 
   * @param arista identificador de la arista a comprobar
   * @return true si el identificador de la arista es válido, 
   * false en caso contrario
   */
  private aristaValida(arista: number): boolean {
    return arista >= 0 && arista < GameService.numAristas
  }


  /**
   * Devuelve true si el hexágono es válido
   * 
   * @param hexagono identificador del hexágono a comprobar
   * @return true si el identificador del hexágono es válido, 
   * false en caso contrario
   */
  private hexagonoValido(hexagono: number): boolean {
    return hexagono >= 0 && hexagono < GameService.numHexagonos
  }


  /**
   * Devuelve un tablero vacío
   * 
   * @Return un tablero vacío
   */
  private tableroVacio(): Tablero {
    console.log("tablero vacio")
    return {
      hexagonos: {
        valor: [],
        tipo: [],
        ladron: 0
      }, 
      vertices: {
        asentamiento: [],
        posible_asentamiento: this.falseArray(GameService.numJugadores, GameService.numVertices),
      }, 
      aristas: {
        camino: [], 
        posible_camino: this.falseArray(GameService.numJugadores, GameService.numAristas),
        puertos: {
          arcilla: 0,
          lana: 0, 
          cereal: 0, 
          basico: [], 
          mineral: 0,
          madera: 0
        }
      }
    }
  }


  /**
   * Funcion de prueba no la uses
   */
  private initPartidaPrueba(){
    this.cargandoPartida = false
    this.partida = {
      miTurno: 1,
      id: "", 
      jugadores: this.inicializarJugadores(["Me!", "Some1", "Some2", "Some3"]),
      turnoActual: 1, 
      totalTurnos: 0,
      tablero: this.tableroPrueba(), 
      resultadoTirada: 7, 
      mensajes: [],
      clock: -1,
      PobladoDisponible: true,
      CiudadDisponible: true, 
      CaminoDisponible: true, 
      movioLadron: false,
      yaComercio: false,
      reported: [false ,false, false, false]
    }

    this.partida.jugadores[0].recursos = {
      madera: 3,
      arcilla: 5,
      mineral: 5, 
      lana: 3, 
      cereales:10
    }

    this.partida.jugadores[1].recursos = {
      madera: 7,
      arcilla: 3,
      mineral: 2, 
      lana: 1, 
      cereales: 8
    }

    this.partida.mensajes.push({ esError:false, remitente: this.partida.jugadores[1], timeStamp: "17:49", body:"Hola a todos!" })
    this.partida.mensajes.push({ esError:false, remitente: this.partida.jugadores[2], timeStamp: "17:49", body:"Hola tío!" })
    this.partida.mensajes.push({ esError:false, remitente: this.partida.jugadores[3], timeStamp: "17:49", body:"Seré el rey de los piratas!" })
    this.partida.mensajes.push({ esError:false, remitente: null, timeStamp: "17:49", body:"Alguien ha hecho algo!" })
    this.partida.mensajes.push({ esError:true, remitente: null, timeStamp: "17:49", body:"Alguien ha hecho algo mu malo nooooo!" })
  }

  /**
   * @return un tablero de prueba para probar euncionamiento 
   * de la interfaz
   */
  private tableroPrueba(): Tablero {
    return {
      hexagonos: {
        valor: [4,10,3,-1,6,5,6,12,11,4,8,9,3,2,9,10,11,5,8],
        tipo: [TipoTerreno.MONTANYA,TipoTerreno.BOSQUE,TipoTerreno.PASTO,TipoTerreno.DESIERTO,TipoTerreno.CERRO,
               TipoTerreno.SEMBRADO,TipoTerreno.PASTO,TipoTerreno.BOSQUE,TipoTerreno.PASTO,TipoTerreno.SEMBRADO,
               TipoTerreno.MONTANYA,TipoTerreno.BOSQUE,TipoTerreno.SEMBRADO,TipoTerreno.BOSQUE,TipoTerreno.SEMBRADO,
               TipoTerreno.CERRO,TipoTerreno.CERRO,TipoTerreno.PASTO,TipoTerreno.MONTANYA],
        ladron: 5
      }, 
      vertices: {
        asentamiento: [TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,
                       TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA,TipoAsentamiento.NADA],
        posible_asentamiento: [
                                [false,false,false,false,false,true,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false],

                                [false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false],
                                 
                                [false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false],
                                 
                                [false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false,false,false,false,false,false,false,
                                 false,false,false,false]
                              ]
      }, 
      aristas: {
        camino: [TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,TipoCamino.NADA,
                 TipoCamino.NADA,TipoCamino.NADA], 
        posible_camino:[
                        [true,true,true,true,true,true,true,true,true,true,
                         true,true,true,true,true,true,true,true,true,true,
                         true,true,true,true,true,true,true,true,true,true,
                         true,true,true,true,true,true,true,true,true,true,
                         true,true,true,true,true,true,true,true,true,true,
                         true,true,true,true,true,true,true,true,true,true,
                         true,true,true,true,true,true,true,true,true,true,
                         true,true],
                        [false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false],
                        [false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false],
                        [false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false,false,false,false,false,false,false,false,false,
                         false,false]
                      ],
        puertos: {
          arcilla: 3,
          lana: 0, 
          cereal: 0, 
          basico: [ 4, 5, 9, 10, 14, 15, 11, 20, 19, 30, 27, 34, 35, 48, 45, 33, 52, 51, 46, 59, 60, 65, 64, 63, 68, 69, 67, 71, 70], 
          mineral: 0,
          madera: 0
        }
      }
    }
  }


  /**
   * Devuelve una matriz de nxm componentes con valor falso
   * 
   * @param n filas
   * @param m columnas
   * @return array de booleanos nxm con todos los valores a falso
   */
  private falseArray(n: number, m: number): Array<Array<Boolean>> {
    let mainArray: Array<Array<Boolean>> = new Array<Array<Boolean>>(n)
    for ( let i = 0; i < n; i++ ){
      mainArray[i] = new Array<Boolean>(m)
      for ( let j = 0; j < m; j++ ){
        mainArray[i][j] = false
      }
    }
    return mainArray
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
      jugadoresPartida.push({
        nombre: jugadores[i],
        turno: i + 1, 
        color: GameService.coloresPorId[i],
        puntos: 0, 
        recursos: { madera: 0, arcilla: 0, cereales: 0, lana: 0, mineral: 0 },
        cartas: { D1: 0, D2: 0, D3: 0, D4: 0, D5: 0, E1: 0, E2: 0},
        primerosAsentamientos: false,
        primerosCaminos: false
      })
    }
    return jugadoresPartida
  }


  /**
   * Actualiza los recursos del jugador dado, con la información en recursos.
   * 
   * @param recursos array de 5 componentes enteras tal que: 
   * [num_madera, num_cereales, num_lana, num_ARCILLA,num_MINERAL]
   * @param playerIndex indice del jugador cuyos recursos se van a actualizar dentro
   * del vector de jugadores de la partida. 
   */
  private actualizarRecursosJugador(recursos: Array<number>, playerIndex: number){
    
    this.partida.jugadores[playerIndex].recursos.madera   = recursos[0]
    this.partida.jugadores[playerIndex].recursos.lana     = recursos[1]
    this.partida.jugadores[playerIndex].recursos.cereales = recursos[2]
    this.partida.jugadores[playerIndex].recursos.arcilla  = recursos[3]
    this.partida.jugadores[playerIndex].recursos.mineral  = recursos[4]
  }


  /**
   * Actualiza los recursos de los jugadores con los datos recibidos.
   * 
   * @param recursos campo "Recursos" del mensaje recibido del servidor. Contiene
   * información actualizada sobre los recursos de los que dispone cada jugador. 
   */
  private actualizarRecursosJugadores(recursos: Object){

    if ( recursos[MessageKeys.PLAYER_1] != null ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_1], 0)
    } else {
      console.log("Faltan los recursos del jugador 1")
    }

    if ( recursos[MessageKeys.PLAYER_2] != null ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_2], 1)
    } else {
      console.log("Faltan los recursos del jugador 2")
    }

    if ( recursos[MessageKeys.PLAYER_3] != null ){
      this.actualizarRecursosJugador(recursos[MessageKeys.PLAYER_3], 2)
    } else {
      console.log("Faltan los recursos del jugador 3")
    }

    if ( recursos[MessageKeys.PLAYER_4] != null ){
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

    if ( cartas[MessageKeys.PLAYER_1] != null ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_1], 0)
    } else {
      console.log("Faltan las cartas del jugador 1")
    }

    if ( cartas[MessageKeys.PLAYER_2] != null ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_2], 1)
    } else {
      console.log("Faltan las cartas del jugador 2")
    }

    if ( cartas[MessageKeys.PLAYER_3] != null ){
      this.actualizarCartasJugador(cartas[MessageKeys.PLAYER_3], 2)
    } else {
      console.log("Faltan las cartas del jugador 3")
    }

    if ( cartas[MessageKeys.PLAYER_4] != null ){
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
    
    if ( puntuacion[MessageKeys.PLAYER_1] != null){
      this.partida.jugadores[0].puntos = puntuacion[MessageKeys.PLAYER_1]
    }else{
      console.log("Falta la puntuación del jugador 1")
    }

    if ( puntuacion[MessageKeys.PLAYER_2] != null){
      this.partida.jugadores[1].puntos = puntuacion[MessageKeys.PLAYER_2]
    }else{
      console.log("Falta la puntuación del jugador 2")
    }

    if ( puntuacion[MessageKeys.PLAYER_3] != null){
      this.partida.jugadores[2].puntos = puntuacion[MessageKeys.PLAYER_3]
    }else{
      console.log("Falta la puntuación del jugador 3")
    }

    if ( puntuacion[MessageKeys.PLAYER_4] != null){
      this.partida.jugadores[3].puntos = puntuacion[MessageKeys.PLAYER_4]
    }else{
      console.log("Falta la puntuación del jugador 4")
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

    if ( msg[MessageKeys.RECURSOS] != null ){
      console.log("Actualizando recursos")
      this.actualizarRecursosJugadores(msg[MessageKeys.RECURSOS])
    }else{
      console.log("Faltan los recursos en el mensaje")
    }

    if ( msg[MessageKeys.CARTAS] != null ){
      console.log("Actualizando cartas")
      this.actualizarCartasJugadores(msg[MessageKeys.CARTAS])
    }else{
      console.log("Faltan las cartas en el mensaje")
    }

    if ( msg[MessageKeys.PUNTUACIONES] != null ){
      console.log("Actualizando puntuaciones")
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

    if ( hexagonos[MessageKeys.HEXAGONOS_TIPOS] != null ){
      console.log( hexagonos[MessageKeys.HEXAGONOS_TIPOS] )
      this.partida.tablero.hexagonos.tipo = hexagonos[MessageKeys.HEXAGONOS_TIPOS]
      console.log(this.partida.tablero.hexagonos.tipo)
    }else{
      console.log("Faltan los tipos de los hexágonos")
    }

    if ( hexagonos[MessageKeys.HEXAGONOS_VALORES] != null ){
      this.partida.tablero.hexagonos.valor = hexagonos[MessageKeys.HEXAGONOS_VALORES]
    }else{
      console.log("Faltan los valores de los hexágonos")
    }

    if ( hexagonos[MessageKeys.HEXAGONOS_LADRON] != null ){
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

    if ( Vertices[MessageKeys.VERTICES_ASENTAMIENTOS] != null ){
      this.partida.tablero.vertices.asentamiento = Vertices[MessageKeys.VERTICES_ASENTAMIENTOS]
    }else{
      console.log("Falta la información sobre asentamientos")
    }

    if ( Vertices[MessageKeys.VERTICES_POSIBLES_ASENTAMIENTOS] != null ){
      let posiblesAsentamientos: Array<Array<boolean>> = Vertices[MessageKeys.VERTICES_POSIBLES_ASENTAMIENTOS]
      for (let i = 0; i < posiblesAsentamientos.length; i++){
          this.partida.tablero.vertices.posible_asentamiento[i] = posiblesAsentamientos[i] 
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

    if ( Aristas[MessageKeys.ARISTAS_CAMINOS] != null ){
      this.partida.tablero.aristas.camino = Aristas[MessageKeys.ARISTAS_CAMINOS]
    }else{
      console.log("Falta la información sobre los caminos")
    }

    if ( Aristas[MessageKeys.ARISTAS_POSIBLES_CAMINOS] != null ){
      let posiblesCaminos: Array<Array<boolean>> = Aristas[MessageKeys.ARISTAS_POSIBLES_CAMINOS]
      for (let i = 0; i < posiblesCaminos.length; i++){
        this.partida.tablero.aristas.posible_camino[i] = posiblesCaminos[i]
      }
    }else{
      console.log("Falta la información sobre posibles caminos")
    }

    if ( Aristas[MessageKeys.ARISTAS_PUERTOS] != null ){
      let puertos: Object = Aristas[MessageKeys.ARISTAS_PUERTOS]
      this.partida.tablero.aristas.puertos.basico   = puertos[MessageKeys.PUERTOS_BASICOS]
      this.partida.tablero.aristas.puertos.madera   = puertos[MessageKeys.PUERTO_MADERA]
      this.partida.tablero.aristas.puertos.mineral  = puertos[MessageKeys.PUERTO_MINERAL]
      this.partida.tablero.aristas.puertos.arcilla  = puertos[MessageKeys.PUERTO_ARCILLA]
      this.partida.tablero.aristas.puertos.lana     = puertos[MessageKeys.PUERTO_LANA]
      this.partida.tablero.aristas.puertos.cereal   = puertos[MessageKeys.PUERTO_CEREAL]
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

    if ( tab_info[MessageKeys.TAB_INFO_HEXAGONOS] != null ){
      this.actualizarHexagonos(tab_info[MessageKeys.TAB_INFO_HEXAGONOS])
    }else{
      console.log("Faltan los hexágonos en el mensaje")
    }

    if ( tab_info[MessageKeys.TAB_INFO_VERTICES] != null ){
      this.actualizarVertices(tab_info[MessageKeys.TAB_INFO_VERTICES])
    }else{
      console.log("Faltan los vértices en el mensaje")
    }

    if ( tab_info[MessageKeys.TAB_INFO_ARISTAS] != null ){
      this.actualizarAristas(tab_info[MessageKeys.TAB_INFO_ARISTAS])
    }else{
      console.log("Faltan las aristas en el mensaje")
    }
  }


  /**
   * Actualiza la información de los primeros asentamientos de los jugadores. 
   * 
   * @param primerosAsentamientos corresponde con el campo "primerosAsentamientos"
   * que se encuentra en el mensaje recibido del servidor.
   */
  private actualiarPrimerosAsentamientos(primerosAsentamientos: Object): void {

    if ( primerosAsentamientos[MessageKeys.PLAYER_1] != null ){
      this.partida.jugadores[0].primerosAsentamientos = primerosAsentamientos[MessageKeys.PLAYER_1]
    }else{
      console.log("Faltan los primeros asentamientos del jugador 1")
    }

    if ( primerosAsentamientos[MessageKeys.PLAYER_2] != null ){
      this.partida.jugadores[1].primerosAsentamientos = primerosAsentamientos[MessageKeys.PLAYER_2]
    }else{
      console.log("Faltan los primeros asentamientos del jugador 2")
    }

    if ( primerosAsentamientos[MessageKeys.PLAYER_3] != null ){
      this.partida.jugadores[2].primerosAsentamientos = primerosAsentamientos[MessageKeys.PLAYER_3]
    }else{
      console.log("Faltan los primeros asentamientos del jugador 3")
    }

    if ( primerosAsentamientos[MessageKeys.PLAYER_4] != null ){
      this.partida.jugadores[3].primerosAsentamientos = primerosAsentamientos[MessageKeys.PLAYER_4]
    }else{
      console.log("Faltan los primeros asentamientos del jugador 4")
    }

  }


  /**
   * Actualiza la información de los primeros caminos de los jugadores. 
   * 
   * @param primerosCaminos corresponde con el campo "primerosCaminos"
   * que se encuentra en el mensaje recibido del servidor.
   */
  private actualiarPrimerosCaminos(primerosCaminos: Object): void {

    if ( primerosCaminos[MessageKeys.PLAYER_1] != null ){
      this.partida.jugadores[0].primerosCaminos = primerosCaminos[MessageKeys.PLAYER_1]
    }else{
      console.log("Faltan los primeros Caminos del jugador 1")
    }

    if ( primerosCaminos[MessageKeys.PLAYER_2] != null ){
      this.partida.jugadores[1].primerosCaminos = primerosCaminos[MessageKeys.PLAYER_2]
    }else{
      console.log("Faltan los primeros Caminos del jugador 2")
    }

    if ( primerosCaminos[MessageKeys.PLAYER_3] != null ){
      this.partida.jugadores[2].primerosCaminos = primerosCaminos[MessageKeys.PLAYER_3]
    }else{
      console.log("Faltan los primeros Caminos del jugador 3")
    }

    if ( primerosCaminos[MessageKeys.PLAYER_4] != null ){
      this.partida.jugadores[3].primerosCaminos = primerosCaminos[MessageKeys.PLAYER_4]
    }else{
      console.log("Faltan los primeros asentamientos del jugador 4")
    }
  }


  /**
   * Genera un mensaje informativo sobre el transcurso de la partida que se 
   * mostrará en el chat de partida.
   * 
   * @param message mensaje a mostrar
   */
  private generarMensajePartida(message: string): void{
    let msg: Mensaje = {
      remitente: null, 
      esError: false,
      timeStamp: null,
      body: message
    }
    this.partida.mensajes.push(msg)
  }


  /**
   * Genera un mensaje de error sobre el transcurso de la partida que se 
   * mostrará en el chat de partida.
   * 
   * @param message mensaje de error a mostrar
   */
  private generarMensajeErrorPartida(message: string): void{
    let msg: Mensaje = {
      remitente: null, 
      esError: true,
      timeStamp: null,
      body: message
    }
    this.partida.mensajes.push(msg)
  }


  /**
   * Actualiza la información de la partida conforme a los nuevos
   * datos recibidos
   * 
   * @param msg mensaje con la nueva información de la partida
   */
  private actualizarPartida(msg: Object): void {

    if (msg[MessageKeys.CLOCK] != null &&
        msg[MessageKeys.CLOCK] > this.partida.clock ){
      
      this.partida.clock = msg[MessageKeys.CLOCK]

      if ( msg[MessageKeys.EXIT_STATUS] <= 0 ) {

        this.generarMensajePartida(msg[MessageKeys.MENSAJE])

        if ( msg[MessageKeys.PLAYER_NAMES] != null ){
          for ( let i = 0; i < GameService.numJugadores; i++){
            this.partida.jugadores[i].nombre = msg[MessageKeys.PLAYER_NAMES][i]
            if ( msg[MessageKeys.PLAYER_NAMES][i] == this.userService.getUsername() ){
              this.partida.miTurno = i + 1
            }
          }
        }else{
          console.log("Faltan los nombres de los jugadores")
        }

        if ( msg[MessageKeys.TURNO_JUGADOR] != null){
          this.partida.turnoActual = msg[MessageKeys.TURNO_JUGADOR] + 1
        }else{
          console.log("Falta el turno de la jugada")
        }

        if ( msg[MessageKeys.TURNO_ACUM] != null){
          this.partida.totalTurnos = msg[MessageKeys.TURNO_ACUM]
        }else{
          console.log("Faltan los turnos totales")
        }

        if ( msg[MessageKeys.RESULTADO_TIRADA] != null ){
          this.partida.resultadoTirada = msg[MessageKeys.RESULTADO_TIRADA]
        } else {
          console.log("Falta el resultado de la tirada")
        }

        if ( msg[MessageKeys.TAB_INFO] != null ){
          this.actualizarTablero(msg[MessageKeys.TAB_INFO])
        }else{
          console.log("Falta la información del tablero")
        }

        this.actualizarJugadores(msg)

        if (msg[MessageKeys.PRIMEROS_ASENTAMIENTOS] != null){
          this.actualiarPrimerosAsentamientos(msg[MessageKeys.PRIMEROS_ASENTAMIENTOS])
        }else{
          console.log("Faltan los primeros asentamientos")
        }

        if (msg[MessageKeys.PRIMEROS_CAMINOS] != null){
          this.actualiarPrimerosCaminos(msg[MessageKeys.PRIMEROS_CAMINOS])
        }else{
          console.log("Faltan los primeros caminos")
        }

        this.actualizarPrecalculos()

      }else{
        this.generarMensajeErrorPartida(msg[MessageKeys.MENSAJE])
        if (  (msg[MessageKeys.MENSAJE] as String).includes("ladron") ) {
          this.partida.movioLadron = false
        }
      }

    } else{
      console.log("Mensaje desordenado, se ignora")
    }
  }


}


@Component({
  selector: 'snack-bar-component-example-snack',
  templateUrl: '../../game/trade-snack-bar.html',
  styleUrls: ['../../game/trade-snack-bar.sass'],
})
export class TradeOfferSnackBar {

  public solicitud: SolicitudComercio
  public gameService: GameService

  constructor(public dialogRef: MatSnackBarRef<TradeOfferSnackBar>,
    @Inject(MAT_SNACK_BAR_DATA) public data: any, public langService: LangService){
    this.solicitud = data["solicitud"]
    this.gameService = data["gameService"]
  }

  public aceptar(){
    this.gameService.aceptarComercioJugador()
    this.dialogRef.dismiss()
  }

  public rechazar(){
    this.gameService.rechazarComercioJugador()
    this.dialogRef.dismiss()
  }
}