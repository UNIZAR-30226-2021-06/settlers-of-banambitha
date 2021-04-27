import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../user/user.service';
import { WsService } from '../ws/ws.service';

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
  CREATED         = "CREATED"
}

/**
 * Estados posibles de una sala
 */
export enum RoomStatus {
  FOUND           = "FOUND",
  SEARCHING       = "SEARCHING",
  FAILED          = "FAILED",
  CLOSED          = "CLOSED",
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
export class RoomService {

  private invitacion_topic_id: any
  private sala_crear_topic_id: any
  private sala_act_topic_id:   any

  //Cliente de stomp
  private stompClient: any

  public room: Room = null

  constructor(private wsService: WsService, private router: Router, private userService: UserService) {
    this.stompClient = wsService.getStompClient() 
    //Suscripción a las invitaciones
    this.invitacion_topic_id = this.stompClient.subscribe(WsService.invitacion_topic + this.userService.getUsername(),
    function (message) {
      console.log(message)
    });

    this.sala_crear_topic_id = this.stompClient.subscribe(WsService.sala_crear_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        this.procesarMensajeCreacionSala(JSON.parse(message.body))
      }else{
        console.log("Error crítico")
      }
    });
  }

  /**
   * Procesa un mensaje de respuesta a la creación de una sala
   * (confirma la creación de la sala)
   * 
   * @param msg mensaje re respuesta ercibido
   */
  private procesarMensajeCreacionSala(msg: Object){
    if ( this.room == null && msg["status"] == RoomMsgStatus.CREATED ) {
      this.room =
      {
        status: RoomStatus.CREATED,
        leader: msg["leader"],
        id: msg["room"],
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

    }
  }


  /**
   * Crea una sala en la que el usuario loggeado (userService) es
   * el líder y único jugador. No hay jugadores invitados. 
   * Solo se puede crear una sala si el jugador todavía no está en ninguna sala
   */
  public crearSala(): void {
    if (this.room != null){
     let msg = { leader: this.userService.getUsername() }
     this.stompClient.send(WsService.salaCrear, {}, JSON.stringify(msg) )
    }
  }


  /**
   * Cierra la sala en la que se encuentra el jugador (si es que se 
   * encuentra en alguna). Solo se puede cerrar la sala si el usuario
   * es el líder de la sala.
   * Si la sala estaba en estado SEARCHING, entonces cancela la búsqueda
   * y cierra la sala (solo si el usuario es el líder).
   */
  public cerrarSala(): void {

  }


  /**
   * Abandona la sala en la que se encuentra el usuario (si es que se encuentra
   * en alguna). Si el usuario es el líder, entonces cierra la sala. 
   * Si el estado de la sala es SEARCHING, entonces cancela la búsqueda y cierra la 
   * sala conforme a lo especificado anteriormente. 
   */
  public abandonarSala(): void {

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
    return true
  }


  /**
   * Cancela una invitación a partida (por parte del receptor).
   * 
   * @param leader líder de la sala que envió la invitación.
   * @param roomId identificador de la sala a la que el usuario fue invitado. 
   */
  public cancelarInvitacion(leader: string, roomId: string): void {

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

  }


  /**
   * Inicia la búsqueda de partida para todos los integrantes de la sala actual. 
   * Solo se puede ejecutar si el líder de la sala es el usuario de la sesión actual. 
   * @return true si se pudo iniciar la búsqueda de partida, false en caso contrario. 
   */
  public buscarPartida(): boolean{
    return true
  }


  /**
   * Si se había iniciado la búsqueda de partida, la cancela. Si no se había iniciado 
   * no hace nada. 
   * @return true si se ha podido cancelar la búsqueda, false si la búsqueda no había empezado
   */
  public cancelarBusqueda(): boolean{
    return true
  }

  // private procesarMensajeActSala(msg: object){
  //   console.log(msg)
  //   switch (msg["status"]){

  //     case WsService.SALA_STATUS_FOUND: 
  //       this.buscandoPartida = false
  //       console.log("---------------------------------------------")
  //       console.log("partida encontrada")
  //       console.log("---------------------------------------------")
  //       break;

  //     case WsService.SALA_STATUS_SEARCHING: 
  //       this.buscandoPartida = true
  //       console.log("---------------------------------------------")
  //       console.log("buscando partida")
  //       console.log("---------------------------------------------")
  //       break;

  //     case WsService.SALA_STATUS_FAILED: 
  //       this.buscandoPartida = false
  //       console.log("---------------------------------------------")
  //       console.log("Busqueda de partida fallida")
  //       console.log("---------------------------------------------")
  //       break;

  //     case WsService.SALA_STATUS_UPDATED_PLAYERS: 
  //       this.jugadoresSala = msg["players"]
  //       console.log("---------------------------------------------")
  //       console.log("Nuevo jugador en la sala")
  //       console.log("---------------------------------------------")
  //       break;

  //     case WsService.SALA_STATUS_UPDATED_INVITES: 
  //       this.jugadoresSala = msg["invites"]
  //       console.log("---------------------------------------------")
  //       console.log("Nuevo jugador invitado")
  //       console.log("---------------------------------------------")
  //       break;
  //   }
  // }


  // public buscarPartida(){
  //   let msg = 
  //   {
  //     leader: this.leaderId,
  //     room: this.salaId
  //   }
  //   this.stompClient.send(WsService.busquedaComenzar, {}, JSON.stringify(msg))
  // }
}
