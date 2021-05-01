import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Injectable, OnDestroy } from '@angular/core';
import { UserService } from '../user/user.service';


export interface Connectable{
  onConnect(): void; 
}

@Injectable({
  providedIn: 'root'
})

/**
 * Clase que encapsula la información básica de la comunicación 
 * por websockets con el servidor. Esta clase es la que gestiona
 * la configuración del cliente de stomp. 
 */
export class WsService implements OnDestroy{

  //Websocket Urls
  private static readonly webSocketEndPoint:  string = 'http://localhost:8080/catan-stomp-ws-ep'
  private static readonly appPrefix:          String = "/app"

  public static readonly salaCrear:          String = WsService.appPrefix + "/sala/crear" 
  public static readonly salaCerrar:         String = WsService.appPrefix + "/sala/cerrar" 
  public static readonly salaAbandonar:      String = WsService.appPrefix + "/sala/abandonar" 
  public static readonly invitacionEnviar:   String = WsService.appPrefix + "/invitacion/enviar"
  public static readonly invitacionCancelar: String = WsService.appPrefix + "/invitacion/cancelar"
  public static readonly invitacionAceptar:  String = WsService.appPrefix + "/invitacion/aceptar"
  public static readonly busquedaComenzar:   String = WsService.appPrefix + "/partida/busqueda/comenzar"
  public static readonly busquedaCancelar:   String = WsService.appPrefix + "/partida/busqueda/cancelar"

  //topics
  public static readonly chat_topic:         string = "/chat/"
  public static readonly invitacion_topic:   string = "/invitacion/"
  public static readonly peticion_topic:     string = "/peticion/"
  public static readonly sala_crear_topic:   string = "/sala-crear/"
  public static readonly sala_act_topic:     string = "/sala-act/"
  public static readonly partida_act_topic:  string = "/partida-act/"
  public static readonly partida_chat_topic: string = "/partida-chat/"
  public static readonly partida_com_topic:  string = "/partida-com/"

  //Cliente de stomp
  private stompClient: any = null;
  private connected: boolean = false
  private observers: Array<Connectable> = []

  /**
   * Constructor: realiza la conexión con el servidor
   */
  constructor(private userService: UserService){
    this._connect()
  }

  /**
   * Método ejecutado al destruir la instancia de la clase. 
   * Realiza la desconexión con el servidor.
   */
  ngOnDestroy(): void {
    this._disconnect()
  }


  /**
   * Devuelve true si ya se ha establecido la conexión con websockets
   */
  public isConnected(): boolean {
    return this.connected
  }

  /**
   * Añade un nuevo observador. Cuando se termine de realizar la conexión
   * de websockets con el servidor, se invocará connectable.onConnect()
   * @param connectable nuevo observer
   * @return true si la conexión todavía no se habñia establecido, y por tanto
   * se invocará connectable.onConnect() en algún momento.
   */
  public atatchConnectable(connectable: Connectable): boolean {
    if ( !this.connected && ! this.observers.includes(connectable)){
      this.observers.push(connectable)
    }
    return !this.connected
  }


  /**
   * Realiza la conexión con el servidor utilizando websockets, 
   * con el protocolo stomp sobre sockjs.
   */
  public _connect(): void {
    console.log("Initialize WebSocket Connection");
    let ws = new SockJS(WsService.webSocketEndPoint);
    this.stompClient = Stomp.over(ws);
    const that = this;

    this.stompClient.ws.onclose = function(event) {
      that._disconnect()
    }

    that.stompClient.connect(this.getWsHeaders(), function (frame) {
      that.observers.forEach( observer => {
        observer.onConnect()
      })
      that.connected = true
      that.observers = null
    }, this.errorCallBack);
  };


  /**
   * Corta la conexión con el servidor solamente si esta se había
   * establecido correctamente. 
   */
  public _disconnect(): void {
    if (this.stompClient !== null) {
        this.stompClient.disconnect();
    }
    console.log("Disconnected");
  }


  /**
   * Función de gestión de error durante el establecimiento de 
   * la conexión. Muestra el error por la consola del navegador. 
   * @param error error que se producjo durante la conexión. 
   */
  public errorCallBack(error): void {
      console.log("errorCallBack -> " + error)
      setTimeout(() => {
          this._connect();
      }, 5000);
  }


  /**
   * Devuelve el cliente stomp utilizado para la comunicación con el servidor. 
   * Si la conexión no se llegó a establecer devuelve un valor nulo. 
   */
  public getStompClient(): any{
    return this.stompClient
  }


  /**
   * Devuelve las cabeceras necesarias para realizar la comunicación con 
   * el servidor de manera correcta a través de websockets. 
   */
  public getWsHeaders(): any{
    return {
      "user-id": this.userService.username
    }
  }

}