import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';
import { GameService } from '../game/game.service';
import { LangService } from '../lang/lang.service';
import { UserService } from '../user/user.service';
import { Connectable, WsService } from '../ws/ws.service';

enum ChatMsgKeys {
  FROM = "from", 
  BODY = "body", 
  TIME = "time"
}

enum PeticionStatus {
  ACCEPT  = "ACCEPT", 
  DECLINE = "DECLINE", 
  REQUEST = "REQUEST",
  TERMINATED = "TERMINATED"
}

export enum StatKeys {
  MAYOR_RACHA          = "mayorRachaDeVictorias", 
  PARTIDAS_JUGADAS     = "partidasJugadas", 
  PORCENTAJE_VICTORIAS = "porcentajeDeVictorias", 
  RACHA_ACTUAL         = "rachaDeVictoriasActual", 
  TOTAL_VICTORIAS      = "totalDeVictorias"
}

export interface Usuario {
  username:            String, 
  avatar:              String,
  rachaActual:         number, 
  numPartidas:         number, 
  mayorRacha:          number, 
  porcentajeVictorias: number,
  totalVictorias:      number
}

export interface PeticionAmistad {
  emisor: String
  ts:     String
}

export interface MsgPrivado {
  emisor: String,
  body:   String,
  ts:     String
}

@Injectable({
  providedIn: 'root'
})
export class SocialService implements Connectable{

  private static readonly baseUrl:  String = environment.baseUrl   + "/amigo"

  public amigos: Array<Usuario> = []
  public peticionesAmistad : Array<PeticionAmistad> = []
  public peticionesAmistadEnviadas: Array<String> = []
  public chats: Map<String, Array<MsgPrivado>> = new Map<String, Array<MsgPrivado>>()

  private myChatTopic_id:   any
  private peticionTopic_id: any
  private stompClient:   any


  /**
   * Constructor. Se suscribe a los topics necesarios para poder gestionar
   * toda la dinámica de salas e invitaciones a estas.
   * @param wsService servicio de websockets a utilzar (singleton)
   * @param router  router de la aplicación
   * @param userService servicio de usuario a utilizar (singleton)
   */
  constructor(private wsService: WsService, private router: Router, private userService: UserService,
              private http: HttpClient, private snackBar: MatSnackBar,
              private langService: LangService) {
    if ( ! wsService.atatchConnectable(this)){
      this.onConnect();
    }
  }

  public openSnackBar(message: string, action: string) {
    this.snackBar.open(message, action);
  }

  public isFriend(user: String): boolean {
    for ( let i = 0; i < this.amigos.length; i++ ){
      if (this.amigos[i].username == user) {
        return true
      }
    }
    return false
  }

  public peticionPendiente(user: String): boolean {
    for ( let i = 0; i < this.peticionesAmistadEnviadas.length; i++ ){
      if (this.peticionesAmistadEnviadas[i] == user) {
        return true
      }
    }
    return false
  }


  public lazyReload(): void {
    let that = this
    setTimeout( () => {
      this.cargarListaAmigos()
      this.cargarPeticiones()
      this.cargarPeticionesEnviadas()
    }, 200)
  }

  public onConnect(): void {
    this.stompClient = this.wsService.getStompClient() 
    let that = this

    this.cargarListaAmigos()
    this.cargarPeticiones()
    this.cargarPeticionesEnviadas()

    //Suscripción a las invitaciones
    this.myChatTopic_id = this.stompClient.subscribe(WsService.chat_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        that.procesarMensajeChat(JSON.parse(message.body))
      }else{
        console.log("Error crítico")
      }
    });

    this.peticionTopic_id = this.stompClient.subscribe(WsService.peticion_topic + this.userService.getUsername(),
    function (message) {
      if (message.body){
        that.procesarMensajePeticion(JSON.parse(message.body))
      }else{
        console.log("Error crítico")
      }
    });
  }

  public pushMensaje(nuevoMensaje: MsgPrivado): void {
    let listaMensajes = this.chats.get(nuevoMensaje.emisor)
    if ( listaMensajes != null ){
      listaMensajes.push(nuevoMensaje)

    }else{
      this.chats.set(nuevoMensaje.emisor, [nuevoMensaje]) 
    }
  }

  public pushMensajePropio(destino: String, body: String){
    let nuevoMensaje: MsgPrivado = {
      emisor: this.userService.username, 
      body: body, 
      ts: null 
    }
    let listaMensajes = this.chats.get(destino)
    if ( listaMensajes != null ){
      listaMensajes.push(nuevoMensaje)

    }else{
      this.chats.set(destino, [nuevoMensaje]) 
    }
  }

  public procesarMensajeChat(msg: Object): void{
    let nuevoMensaje: MsgPrivado = {
      emisor: msg[ChatMsgKeys.FROM],
      body:   msg[ChatMsgKeys.BODY],
      ts:     msg[ChatMsgKeys.TIME]
    }
    this.pushMensaje(nuevoMensaje)
  }


  public procesarMensajePeticion(msg: Object): void{
    console.log(msg)
    switch (msg["type"]){
      case PeticionStatus.ACCEPT:
        //Recargar lista de amigos
        this.cargarListaAmigos()
        this.cargarPeticionesEnviadas()
        this.openSnackBar(msg[ChatMsgKeys.FROM] + " "  + this.langService.get("accepted-friendship"), "OK")
        break

      case PeticionStatus.DECLINE:
        //No pasa nada
        this.cargarPeticiones()
        this.cargarPeticionesEnviadas()
        this.openSnackBar(msg[ChatMsgKeys.FROM] + " "  + this.langService.get("declined-friendship"), "OK")
        break

      case PeticionStatus.REQUEST:
        this.peticionesAmistad.push({
          emisor: msg[ChatMsgKeys.FROM],
          ts:     msg[ChatMsgKeys.TIME]
        })
        this.openSnackBar(msg[ChatMsgKeys.FROM] + " "  + this.langService.get("wants-friendship"), "OK")
        break

      case PeticionStatus.TERMINATED: 
        this.lazyReload()
        break;

      default: 
        console.log("mensaje desconocido")
    }
  }


  public aceptarPeticionAmistad(user: String): void {
      let msg = {
        from: this.userService.username,
        to:   user
      }
      this.stompClient.send(WsService.aceptarPeticion, {}, JSON.stringify(msg) )
      this.lazyReload()
  }


  public rechazarPeticionAmistad(user: String): void {
      let msg = {
        from: this.userService.username,
        to:   user
      }
      this.stompClient.send(WsService.rechazarPeticion, {}, JSON.stringify(msg) )
      this.lazyReload()
  }


  public enviarPeticionAmistad(user: String): void {
      let msg = {
        from: this.userService.username,
        to:   user
      }
      console.log(msg)
      this.stompClient.send(WsService.enviarPeticion, {}, JSON.stringify(msg) )
  }


  public enviarMensajePrivado(user: String, body: String): void{
      let msg = {
        from: this.userService.username,
        to:   user,
        body: body
      }
      console.log(msg)
      this.stompClient.send(WsService.enviarMensajePrivado, {}, JSON.stringify(msg) )
      this.pushMensajePropio(user, body)
  }

  public cargarListaAmigos(): void {

    let that = this
    this.amigos = []
    this.http.get(SocialService.baseUrl + "/list/" + this.userService.username).subscribe( (response) => {
      for ( var index in response ){
        let friend: Usuario = {
          username: response[index]["usuario2_id"],
          avatar: null, 
          mayorRacha: 0, 
          rachaActual: 0, 
          porcentajeVictorias: 0, 
          numPartidas: 0, 
          totalVictorias: 0
        }

        this.userService.findUserObservable(friend.username).subscribe( (resp) => {
          friend.username = resp["nombre"]
          friend.avatar   = resp["avatar"]
          that.userService.getUserStatsObservable(friend.username).subscribe (
            (stats) => {
              that.updateUserStats(stats, friend)
              that.amigos.push(friend)
            }
          )
        }, (e) => { })
      }
    })
  }


  public cargarPeticiones(): void {

    let that = this
    this.peticionesAmistad = []
    this.http.get(SocialService.baseUrl + "/pending-r/" + this.userService.username).subscribe( (response) => {
      for ( var index in response ){
        let peticion: PeticionAmistad = {
          emisor: response[index]["from"],
          ts: null 
        }
        that.peticionesAmistad.push(peticion)
      }
    })
  }

  public cargarPeticionesEnviadas(): void {

    let that = this
    this.peticionesAmistadEnviadas = []
    this.http.get(SocialService.baseUrl + "/pending-s/" + this.userService.username).subscribe( (response) => {
      for ( var index in response ){
        that.peticionesAmistadEnviadas.push( response[index]["to"])
      }
    })
  }

  /**
   * Actualiza la información del usuario con respecto al 
   * mensaje recibido (stats).
   * 
   * @param stats 
   * @param user 
   */
  public updateUserStats(stats: Object, user: Usuario): void{
    user.rachaActual         = stats[StatKeys.RACHA_ACTUAL]
    user.totalVictorias      = stats[StatKeys.TOTAL_VICTORIAS]
    user.mayorRacha          = stats[StatKeys.MAYOR_RACHA]
    user.numPartidas         = stats[StatKeys.PARTIDAS_JUGADAS]
    user.porcentajeVictorias = stats[StatKeys.PORCENTAJE_VICTORIAS]
  }

}
