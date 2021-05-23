import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { GameService } from '../game/game.service';
import { UserService } from '../user/user.service';
import { Connectable, WsService } from '../ws/ws.service';

/**
 * Estados de un mensaje de acción de una sala en concreto
 */
enum RoomMsgStatus {
  FOUND           = "FOUND",
  SEARCHING       = "SEARCHING",
  FAILED          = "FAILED",
  UPDATED_PLAYERS = "UPDATED_PLAYERS",
  UPDATED_INVITES = "UPDATED_INVITES",
  CLOSED          = "CLOSED",
  CREATED         = "CREATED",
  CANCELLED       = "CANCELLED"
}

enum InviteMsgstatus {
  INVITED     = "INVITED", 
  ACCEPTED    = "ACCEPTED",
  CANCELLED   = "CANCELLED",
  FULL        = "FULL",
  QUEUING     = "QUEUING",
  CLOSED      = "CLOSED",
  OPEN        = "OPEN"
}

/**
 * Estados posibles de una sala
 */
export enum RoomStatus {
  SEARCHING       = "SEARCHING",
  CREATED         = "CREATED"
}

/**
 * Información básica de un usuario para
 * mostrar en la pantalla de espera de sala
 */
export interface UserCardInfo {
  username: String
  avatar: String
}

/**
 * Información de la sala
 */
export interface Room {
  leader:  String
  id:      String
  invites: Array<string>
  players: Array<UserCardInfo>
  status:  RoomStatus
}

/**
 * Información de una invitación
 */
export interface invite {
  leader: String
  id:     String
}

@Injectable({
  providedIn: 'root'
})
/**
 * Servicio que gestiona todos los aspectos relacionados con la
 * sala de espera previa a una partida: búsqueda de partida, invitaciones
 * y creación de salas. 
 */
export class RoomService implements Connectable{

  private invitacion_topic_id: any
  private sala_crear_topic_id: any
  private sala_act_topic_id:   any

  //Cliente de stomp
  private stompClient: any

  //Sala
  public room:               Room = null
  public invites:            Array<invite> = []
  public buscandoPartida:    boolean = false
  public uniendoseASala:     boolean = false
  public creandoSala:        boolean = false
  public liderCerroSala:     boolean = false
  public errorAlUnirseASala: boolean = false;


  /**
   * Constructor. Se suscribe a los topics necesarios para poder gestionar
   * toda la dinámica de salas e invitaciones a estas.
   * @param wsService servicio de websockets a utilzar (singleton)
   * @param router  router de la aplicación
   * @param userService servicio de usuario a utilizar (singleton)
   */
  constructor(private wsService: WsService, private router: Router, private userService: UserService, private gameService: GameService) {
    if ( ! wsService.atatchConnectable(this)){
      this.onConnect();
    }
  }

  /**
   * Función a llamar cuando la conexión websocket con el 
   * servidor se haya establecido
   */
  public onConnect(): void{
    this.stompClient = this.wsService.getStompClient() 
    let that = this
    //Suscripción a las invitaciones
    this.invitacion_topic_id = this.stompClient.subscribe(WsService.invitacion_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        that.procesarMensajeInvitacion(JSON.parse(message.body))
      }else{
        that.room = null 
        console.log("Error crítico")
      }
    });

    this.sala_crear_topic_id = this.stompClient.subscribe(WsService.sala_crear_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        that.procesarMensajeCreacionSala(JSON.parse(message.body))
      }else{
        that.room = null 
        console.log("Error crítico")
      }
    });

    this.crearSala()
  }


  /**
   * Devuelve true si el usuario es el líder de la sala. Si no 
   * existe la sala o existe pero no es el líder, devuelve false. 
   */
  private soyLider(): boolean{
    return this.room != null && this.room.leader == this.userService.getUsername()
  }


  /**
   * Devuelve true si se inició la búsqueda de partida en la sala
   */
  private busquedaIniciada(): boolean{
    return this.room != null && this.room.status == RoomStatus.SEARCHING
  }


  /**
   * Procesa un mensaje de respuesta a la creación de una sala
   * (confirma la creación de la sala)
   * 
   * @param msg mensaje re respuesta ercibido
   */
  private procesarMensajeCreacionSala(msg: Object){
    console.log(msg)
    if ( this.room == null && msg["status"] == RoomMsgStatus.CREATED ) {
      this.room =
      {
        status:  RoomStatus.CREATED,
        leader:  msg["leader"],
        id:      msg["room"],
        invites: msg["invites"],
        players: [
          {
            username: this.userService.username,
            avatar: this.userService.avatar
          }
        ]
      }

      this.sala_act_topic_id = this.stompClient.subscribe(WsService.sala_act_topic + this.room.id, (message) => {
        if ( message.body ){
          this.procesarMensajeAccionSala(JSON.parse(message.body))
        }
      })
      this.creandoSala = false
      console.log("sala creada")
    }
  }

  /**
   * Actualiza la lista de usuarios de la sala. Para 
   * los usuarios de los que no se dispone avatar, los solicita.
   * 
   * @param updated_players lista de jugadores de la sala actualizada
   */
  private updatePlayers(updated_players: Array<String>): void{
    if ( this.room != null){
      let old_players: Array<UserCardInfo> = this.room.players
      this.room.players = []
      for (let i = 0; i < updated_players.length; i++){
        let found: boolean = false
        for (let j = 0; j < this.room.players.length; j++){
          if ( old_players[j].username == updated_players[i]){
            found = true
            this.room.players.push(old_players[j])
            break
          }
        }
        if ( !found ){
          //El jugador no estaba en la sala, se añade y 
          //se busca su avatar
          this.room.players.push({username: updated_players[i], avatar: null})
          this.userService.findUserObservable(updated_players[i]).toPromise().then( response => {
            let body = response["body"]
            console.log(body)
            if ( body ){
              let avatar = body["avatar"]
              let username = body["nombre"]
              for (let k = 0; k < this.room.players.length; i++){
                if (this.room.players[k].username == username){
                  this.room.players[k].avatar = avatar
                  break
                }
              }
            }
          })
        }
      }
    }

  }

  
  /**
   * Procesa un mensaje de acción recibido para la sala en la
   * que se encuentra actualmente el jugador
   * 
   * @param msg mensaje recibido
   */
  private procesarMensajeAccionSala(msg: Object){
    if ( this.room != null ){
      switch (msg["status"]){
        case RoomMsgStatus.CREATED: 
          //No debería de llegar nunca un mensaje de este 
          //tipo al endpoint de accion sala, pero por si acaso
          if ( this.room == null ){
            this.procesarMensajeCreacionSala(msg)
          }
          break

        case RoomMsgStatus.CLOSED: 
          //El líder ha cerrado la sala
          this.liderCerroSala = true
          this.sala_act_topic_id.unsubscribe()
          this.room = null
          this.buscandoPartida = false
          this.crearSala()
          break

        case RoomMsgStatus.SEARCHING: 
          //Ha iniciado la búsqueda de partida
          this.buscandoPartida = true
          this.room.status = RoomStatus.SEARCHING
          console.log("buscando partida")
          break

        case RoomMsgStatus.UPDATED_INVITES: 
          //Se ha actualizado la lista de usuarios inviatdos
          this.room.invites = msg["invites"]
          break

        case RoomMsgStatus.UPDATED_PLAYERS: 
          //Se ha actualizado la lista de jugadores de la sala
          let updated_players: Array<String> = msg["players"]
          this.updatePlayers(updated_players)
          break

        case RoomMsgStatus.FOUND: 
          //Se ha encontrado partida
          this.gameService.comenzarPartida(msg["game"], msg["players"], this);
          this.room = null
          this.buscandoPartida = false
          this.sala_act_topic_id.unsubscribe()
          break

        case RoomMsgStatus.FAILED: 
          //Algo ha ido mal
          this.room = null
          this.buscandoPartida = false
          this.crearSala()
          break
        
        case RoomMsgStatus.CANCELLED: 
          this.room.status = RoomStatus.CREATED
          this.buscandoPartida = false

        default: 
      }
    }
  }

  /**
   * Procesa los mensajes que llegan al endpoint /invitacion/<user-id>
   * @param msg mensaje recibido
   */
  private procesarMensajeInvitacion(msg: Object){
    let invitacion: invite; 

    switch (msg["status"]){
      case InviteMsgstatus.INVITED: 
        //Llega una nueva invitación
        invitacion = {
          leader: msg["leader"],
          id: msg["room"]
        }
        if ( ! this.invites.includes(invitacion)){
          this.invites.push(invitacion)
        }
        break 

      case InviteMsgstatus.ACCEPTED: 
        //Se aceptó la invitación
        this.uniendoseASala = false
        this.room =
        {
          status:  RoomStatus.CREATED,
          leader:  msg["leader"],
          id:      msg["room"],
          invites: msg["invites"],
          players: [
            {
              username: this.userService.username,
              avatar: this.userService.avatar
            }
          ]
        }
        
        let updated_players: Array<String> = msg["players"]
        this.updatePlayers(updated_players)
        this.sala_act_topic_id = this.stompClient.subscribe(WsService.sala_act_topic + this.room.id, (message) => {
          if ( message.body ){
            this.procesarMensajeAccionSala(JSON.parse(message.body))
          }
        })
        break 

      case InviteMsgstatus.OPEN: 
        this.uniendoseASala = false
        this.errorAlUnirseASala = true
        this.crearSala()
        break 

      case InviteMsgstatus.CANCELLED: 
        invitacion = {
          leader: msg["leader"],
          id: msg["room"]
        }
        if ( this.invites.includes(invitacion)){
          this.invites.splice(this.invites.indexOf(invitacion),1)
        }
        break 

      case InviteMsgstatus.CLOSED: 
        this.uniendoseASala = false
        this.errorAlUnirseASala = true
        this.crearSala()
        break 

      case InviteMsgstatus.FULL: 
        this.uniendoseASala = false
        this.errorAlUnirseASala = true
        this.crearSala()
        break 

      case InviteMsgstatus.QUEUING: 
        this.uniendoseASala = false
        this.errorAlUnirseASala = true
        this.crearSala()
        break 

      default: 
    }
  }


  /**
   * Crea una sala en la que el usuario loggeado (userService) es
   * el líder y único jugador. No hay jugadores invitados. 
   * Solo se puede crear una sala si el jugador todavía no está en ninguna sala
   */
  public crearSala(): void {
    if (this.room == null && !this.uniendoseASala && !this.creandoSala){
      let msg = { leader: this.userService.getUsername() }
      this.stompClient.send(WsService.salaCrear, {}, JSON.stringify(msg) )
      this.creandoSala = true
    }
  }


  /**
   * Cierra la sala en la que se encuentra el jugador (si es que se 
   * encuentra en alguna). Solo se puede cerrar la sala si el usuario
   * es el líder de la sala.
   * Si la sala estaba en estado SEARCHING, entonces cancela la búsqueda
   * y cierra la sala (solo si el usuario es el líder).
   * @param crearSala: indica si se debe tratar de crear una nueva sala o no.
   */
  public cerrarSala(crearSala: boolean): void {
    if ( this.soyLider() ){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id
      }
      this.stompClient.send(WsService.salaCerrar, {}, JSON.stringify(msg) )
      this.sala_act_topic_id.unsubscribe()
      this.buscandoPartida = false
      this.uniendoseASala = false
      this.room = null
      if ( crearSala ){
        this.crearSala()
      }
    }
  }


  /**
   * Abandona la sala en la que se encuentra el usuario (si es que se encuentra
   * en alguna). Si el usuario es el líder, entonces cierra la sala. 
   * Si el estado de la sala es SEARCHING, entonces cancela la búsqueda y cierra la 
   * sala conforme a lo especificado anteriormente. 
   * @param crearSala: indica si se debe tratar de crear una nueva sala o no.
   */
  public abandonarSala(crearSala: boolean): void {
    if ( this.room != null && !this.soyLider() ){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id,
        player: this.userService.getUsername()
      }
      this.stompClient.send(WsService.salaAbandonar, {}, JSON.stringify(msg) )
      this.sala_act_topic_id.unsubscribe()
      this.buscandoPartida = false
      this.uniendoseASala = false
      this.room = null
      if ( crearSala ){
        this.crearSala()
      }
    }
  }


  /**
   * Envía una invitación al usuario con el identificador dado.
   * 
   * @param username nombre del usuario al que se envía la invitación. El usuario debe existir
   * o la operación no tendrá efecto. 
   * @return true si el usuario es el líder de la sala y se ha podido enviar la invitación, false 
   * en caso contrario. 
   */
  public enviarInvitacion(username: string): boolean {
    if ( this.soyLider() ){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id, 
        invite: username
      }
      this.stompClient.send(WsService.invitacionEnviar, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }


  /**
   * Cancela una invitación a partida (por parte del emisor).
   * 
   * @param leader líder de la sala que envió la invitación.
   * @param roomId identificador de la sala a la que el usuario fue invitado. 
   */
  public cancelarInvitacion(leader: string, roomId: string): void {
    if ( this.soyLider() ){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id, 
        invite: this.userService.getUsername()
      }
      this.stompClient.send(WsService.invitacionCancelar, {}, JSON.stringify(msg) )
    }
  }


  /**
   * Acepta la invitación a una sala y espera para cargar los datos de la misma.
   * Si el usuario ya estaba en una sala, entonces sale de la sala y 
   * se une a la sala de invitación aceptada. Hasta que lleguen los datos de 
   * la nueva sala el usuario no estará en ninguna sala. 
   * 
   * @param leader líder de la sala a la que el usuario fue invitado
   * @param roomId identificador de la sala a la que el usuario fue invitado
   */
  public aceptarInvitacion(leader: string, roomId: string): void {
    if ( leader != null && roomId != null && !this.creandoSala && !this.uniendoseASala){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id, 
        invite: this.userService.getUsername()
      }
      this.stompClient.send(WsService.invitacionAceptar, {}, JSON.stringify(msg) )
      if ( this.soyLider() ){
        this.cerrarSala(false)
      }else if ( this.room != null){
        this.abandonarSala(false)
      }
      this.uniendoseASala = true
    }
  }


  /**
   * Inicia la búsqueda de partida para todos los integrantes de la sala actual. 
   * Solo se puede ejecutar si el líder de la sala es el usuario de la sesión actual. 
   * @return true si se pudo iniciar la búsqueda de partida, false en caso contrario. 
   */
  public buscarPartida(): boolean{
    if (this.soyLider() && !this.busquedaIniciada()){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id
      }
      this.stompClient.send(WsService.busquedaComenzar, {}, JSON.stringify(msg) )
      return true 
    }
    return false
  }


  /**
   * Si se había iniciado la búsqueda de partida, la cancela. Si no se había iniciado 
   * no hace nada. 
   * @return true si se ha podido cancelar la búsqueda, false si la búsqueda no había empezado
   */
  public cancelarBusqueda(): boolean{
    if ( this.busquedaIniciada() ){
      let msg = {
        leader: this.room.leader, 
        room: this.room.id,
        player: this.userService.getUsername()
      }
      this.stompClient.send(WsService.busquedaCancelar, {}, JSON.stringify(msg) )
      return true
    }
    return false
  }
}
