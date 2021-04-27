import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Injectable, OnDestroy } from '@angular/core';
import { UserService } from '../user/user.service';
import { NavigationEnd, Router } from '@angular/router';
import { filter } from 'rxjs/operators';

type subFunction = () => void; 

@Injectable({
  providedIn: 'root'
})
export class WsService implements OnDestroy{


  //Websocket Urls
  private static readonly webSocketEndPoint:  string = 'http://localhost:8080/catan-stomp-ws-ep'
  private static readonly appPrefix:          String = "/app"
  private static readonly salaCrear:          String = WsService.appPrefix + "/sala/crear" 
  private static readonly salaCerrar:         String = WsService.appPrefix + "/sala/cerrar" 
  private static readonly invitacionEnviar:   String = WsService.appPrefix + "/invitacion/enviar"
  private static readonly invitacionCancelar: String = WsService.appPrefix + "/invitacion/cancelar"
  private static readonly invitacionAceptar:  String = WsService.appPrefix + "/invitacion/aceptar"
  private static readonly busquedaComenzar:   String = WsService.appPrefix + "/partida/busqueda/comenzar"
  private static readonly busquedaCancelar:   String = WsService.appPrefix + "/partida/busqueda/cancelar"

  //Estados de una sala de búsqueda
  private static SALA_STATUS_FOUND:             string  = "FOUND"
  private static SALA_STATUS_SEARCHING:         string  = "SEARCHING"
  private static SALA_STATUS_FAILED:            string  = "FAILED"
  private static SALA_STATUS_UPDATED_PLAYERS:   string  = "UPDATED_PLAYERS"
  private static SALA_STATUS_UPDATED_INVITES:   string  = "UPDATED_INVITES"

  //topics
  private chat_topic:         string = "/chat"
  private invitacion_topic:   string = "/invitacion"
  private peticion_topic:     string = "/peticion"
  private sala_crear_topic:   string = "/sala-crear"
  private sala_act_topic:     string = "/sala-act"
  private partida_act_topic:  string = "/partida-act"
  private partida_chat_topic: string = "/partida-chat"
  private partida_com_topic:  string = "/partida-com"

  //Subscription ids
  private chat_topic_id:         any
  private invitacion_topic_id:   any
  private peticion_topic_id:     any
  private sala_crear_topic_id:   any
  private sala_act_topic_id:     any
  private partida_act_topic_id:  any
  private partida_chat_topic_id: any
  private partida_com_topic_id:  any


  //Variables del estado de la sala
  private partidaId: string = null          //identificador de la partida en curso (si existe)
  private salaId:    string = null          //identificador de la sala en la que se encuentra el jugador
  private leaderId:  string = null          //Líder de la sala
  private jugadoresSala: Array<string> = [] //Jugadores en la sala
  private invitadosSala: Array<string> = [] //Jugadores invitados a la sala

  //Variables de estado
  private buscandoPartida: boolean = false
  private esLider: boolean = false

  //Cliente de stomp
  private stompClient: any = null;


  constructor( private userService: UserService, private router: Router){
    this.chat_topic       += "/" + this.userService.getUsername()
    this.invitacion_topic += "/" + this.userService.getUsername()
    this.peticion_topic   += "/" + this.userService.getUsername()
    this.sala_crear_topic += "/" + this.userService.getUsername()
    this._connect()
  }

  ngOnDestroy(): void {
    this._disconnect()
  }


  public _connect() {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(WsService.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const that = this;

    this.stompClient.ws.onclose = function(event) {
      that._disconnect()
    }

    that.stompClient.connect({}, function (frame) {
      console.log("Connected")

      this.chat_topic_id = that.stompClient.subscribe(this.chat_topic, function (message) {
        console.log(message)
      });

      this.invitacion_topic_id = that.stompClient.subscribe(this.invitacion_topic, function (message) {
        console.log(message)
      });

      this.peticion_topic_id = that.stompClient.subscribe(this.peticion_topic, function (message) {
        console.log(message)
      });

      console.log("Subscribed to chat, invitacion and peticion")

    }, this.errorCallBack);
  };


  public _disconnect() {
    if (this.stompClient !== null) {
        let msg = 
        {
          leader: this.leaderId,
          room: this.salaId,
          player: this.userService.getUsername()
        }
        if ( this.buscandoPartida ){
          this.stompClient.send(WsService.busquedaCancelar, {}, JSON.stringify(msg))
        }
        if ( this.esLider ){
          //El jugador estaba en una sala y es el lider
          this.stompClient.send(WsService.salaCerrar, {}, JSON.stringify(msg))
        }
        this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }


  // on error, schedule a reconnection attempt
  public errorCallBack(error) {
      console.log("errorCallBack -> " + error)
      setTimeout(() => {
          this._connect();
      }, 5000);
  }


  public crearSala(){
    const that = this
    this.sala_crear_topic_id = this.stompClient.subscribe(this.sala_crear_topic, (message) => {
      if ( message.body ){

        let obj = JSON.parse(message.body)
        this.procesarMensajeCreacionSala(obj)

      }else{

        console.log("Error crítico")

      }

    });

    let msg = { leader: this.userService.getUsername() }
    this.stompClient.send(WsService.salaCrear, {}, JSON.stringify(msg) )
  }


  private procesarMensajeCreacionSala(msg: object){
    this.salaId        = msg["room"]
    this.leaderId      = msg["leader"]
    this.jugadoresSala = msg["players"]
    this.invitadosSala = msg["invites"]

    this.sala_crear_topic_id.unsubscribe()
    this.sala_act_topic_id = this.stompClient.subscribe(this.sala_act_topic + "/" + this.salaId, (message) => {
      console.log(message)
      if ( message.body ){
        this.procesarMensajeActSala(JSON.parse(message.body))
      }
    })

  }


  private procesarMensajeActSala(msg: object){
    console.log(msg)
    switch (msg["status"]){

      case WsService.SALA_STATUS_FOUND: 
        this.buscandoPartida = false
        console.log("---------------------------------------------")
        console.log("partida encontrada")
        console.log("---------------------------------------------")
        break;

      case WsService.SALA_STATUS_SEARCHING: 
        this.buscandoPartida = true
        console.log("---------------------------------------------")
        console.log("buscando partida")
        console.log("---------------------------------------------")
        break;

      case WsService.SALA_STATUS_FAILED: 
        this.buscandoPartida = false
        console.log("---------------------------------------------")
        console.log("Busqueda de partida fallida")
        console.log("---------------------------------------------")
        break;

      case WsService.SALA_STATUS_UPDATED_PLAYERS: 
        this.jugadoresSala = msg["players"]
        console.log("---------------------------------------------")
        console.log("Nuevo jugador en la sala")
        console.log("---------------------------------------------")
        break;

      case WsService.SALA_STATUS_UPDATED_INVITES: 
        this.jugadoresSala = msg["invites"]
        console.log("---------------------------------------------")
        console.log("Nuevo jugador invitado")
        console.log("---------------------------------------------")
        break;
    }
  }


  public buscarPartida(){
    let msg = 
    {
      leader: this.leaderId,
      room: this.salaId
    }
    this.stompClient.send(WsService.busquedaComenzar, {}, JSON.stringify(msg))
  }

}