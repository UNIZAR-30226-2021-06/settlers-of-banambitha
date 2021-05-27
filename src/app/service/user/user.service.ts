import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { catchError, retry, map } from 'rxjs/operators';
import { environment } from '../../../environments/environment';
import { GameService } from '../game/game.service';
import { WsService } from '../ws/ws.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LangService } from '../lang/lang.service';

export enum BoardSkin {
  CLASICA  = "Clasica", 
  HARDWARE = "Hardware",
  ESPACIAL = "Espacial" 
}



@Injectable({
  providedIn: 'root'
})

/**
 * Clase que implementa las peticiones al servidor relacionadas con el usuario, 
 * y almacena la información de sesión del usuario.
 */
export class UserService {

  private static readonly baseUrl = environment.baseUrl + "/usuario"
  private static readonly addUrl = UserService.baseUrl + "/add"
  private static readonly validate = UserService.baseUrl + "/validate"
  private static readonly stats =  UserService.baseUrl + "/stats"
  private static readonly update =  UserService.baseUrl + "/update"
  private static readonly changePass = UserService.baseUrl + "/new-password";
  private static readonly httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    withCredentials: true,
    observe: 'response' as 'response'
  }

  //Atributos relacionados con el usuario
  //son públicos para permitir data binding en el 
  //resto de la aplicación, pero deben modificarse 
  //con setters
  public username:   String
  public mail:       String
  public idioma:     String
  public avatar:     String
  public apariencia: BoardSkin
  public saldo:      Number
  public partida:    string
  public bloqued:    String
  public reports:    number
  public validUser:  boolean = false
  public changePass: boolean = false

  constructor(private http: HttpClient, public snackBar: MatSnackBar, private langService: LangService) {}

  //Getters
  public getUsername(): String {
    return this.username;
  }

  public getMail(): String {
    return this.mail;
  }

  public getIdioma(): String {
    return this.idioma;
  }

  public getAvatar(): String {
    return this.avatar;
  }

  public getApariencia(): String {
    return this.apariencia;
  }

  public getSaldo(): Number {
    return this.saldo;
  }

  public logedUser(): Boolean {
    return this.validUser
  }

  public setSaldo(nuevoSaldo: Number){
    this.saldo = nuevoSaldo
  }

  public setAvatar(avatar_url: String){
    this.avatar = avatar_url
  }

  public getChangePass(){
    return this.changePass
  }

  public updateUserData(userData: Object){
    console.log(userData)
    this.username   = userData["nombre"]
    this.saldo      = userData["saldo"]
    this.mail       = userData["email"]
    this.avatar     = userData["avatar"]
    this.partida    = userData["partida"]
    this.bloqued    = userData["bloqueado"]
    this.reports    = userData["informes"]
    this.langService.setSelectedLang(userData["idioma"])
    this.apariencia = this.getSkinFromUrl(userData["apariencia"])
  }


  public getSkinFromUrl(url: String): BoardSkin{
    switch (url){
      case "espacial_shop_image.jpg": 
        return BoardSkin.ESPACIAL; 

      case "hardware_shop_image.jpg": 
        return BoardSkin.HARDWARE; 

      default: 
        return BoardSkin.CLASICA; 
    }
  }

  public getUrlFromSkin(skin: BoardSkin): String {
    switch (skin){

      case BoardSkin.HARDWARE: 
        return "hardware_shop_image.jpg" 

      case BoardSkin.ESPACIAL: 
        return "espacial_shop_image.jpg"

      default: 
        return "apariencia_clasica.png"

    }
  }

  public updateApariencia(skin: BoardSkin){
    let msg = {
      nombre : this.username,
      apariencia : this.getUrlFromSkin(skin)
    }

    let that = this

    this.http.put<any>(UserService.baseUrl + "/update", JSON.stringify(msg),
                                             UserService.httpOptions).subscribe( (response) => {

      console.log("apariencia actualizada a " + skin)
      that.updateUserData(response.body)
    })
  }


  public updateIdioma(nuevoIdioma: String): void {
    let msg = {
      nombre : this.username,
      idioma : nuevoIdioma
    }

    let that = this

    this.http.put<any>(UserService.baseUrl + "/update", JSON.stringify(msg),
                                             UserService.httpOptions).subscribe( (response) => {

      that.updateUserData(response.body)
    })
  }


  /**
   * Trata de registrar un nuevo usuario en el sistema. Si se produce
   * algún error, lanza una excepción.
   * @param name nombre del usuario
   * @param mail mail del usuario
   * @param pass contraseña del usuario
   */
  public async register(name: String, mail: String, pass: String){
    let msg = {
      nombre : name,
      email : mail,
      contrasenya : pass
    }
    let response = await this.http.post<any>(UserService.baseUrl + "/add", JSON.stringify(msg),
                                             UserService.httpOptions).toPromise()
    this.updateUserData(response.body)
    return response
  }

  /**
   * Cierra la sesión
   */
  public logout(router: Router){
    console.log("logout")
    this.http.get(UserService.baseUrl + "/logout", UserService.httpOptions).subscribe((data: any) => {
      router.navigate(["/login"])
      window.location.reload()
    })
  }

  /**
   * Devuelve la promise de una petición que solicita la 
   * búsqueda de un usuario
   * @param name 
   */
  public findUserObservable(name: String){
    return this.http.get(UserService.baseUrl + "/find/" + name)
  }


  /**
   * Devuelve la promise de una petición que solicita las
   * estadísticas de un usuario
   * @param name 
   */
  public getUserStatsObservable(name: String){
    return this.http.get(UserService.baseUrl + "/stats/" + name)
  }


  /**
   * Trata de validar el usuario y contraseña. Si el usuario dado
   * tiene la conraseña dada, entonces actualiza los datos de usuario. 
   * En caso contrario lanza una excepción. 
   * @param name 
   * @param pass 
   */
  public async validate(name: String, pass: String){
    let msg = {
      "nombre" : name,
      "contrasenya" : pass
    }
    let response = await this.http.post(UserService.baseUrl + "/validate", JSON.stringify(msg),
                                        UserService.httpOptions).toPromise()
    this.updateUserData(response.body)
  }


  /**
   * Devuelve un valor booleano observable cuyo valor es: 
   *  - true si urlLogin y el usuario no ha iniciado sesión o si !urlLogin 
   *    pero el usuario ya ha iniciado sesión.  
   *  - False en cualquier otro caso
   * El valor booleano determina si se puede navegar a la ruta solicitada
   * Función auxiliar para los guards del router
   * @param router 
   * @param urlLogin True si la url solicitada es login o hijas, false en caso 
   *                 contrario
   */
  public checkSession(router: Router, urlLogin: boolean, gameService: GameService, wsService: WsService): Observable<boolean>{

    return this.http.get(UserService.baseUrl + "/session", UserService.httpOptions).pipe(
      map(response => {
        this.updateUserData(response.body)
        wsService._connect()
        if ( this.partida != null ){
          gameService.recargarPartida(this.partida)
          router.navigate(["/board"])
          return false

        }else if ( this.bloqued != null ){
          console.log("Cuenta bloqueada!")
          router.navigate(["/banned"])
          return false

        }else{
          if ( urlLogin ){
            router.navigate(["/home"])
            return false
          } else {
            return true
          }
        }
      }
    ), catchError((e) => {
      console.log("EXCEPCION-------")
      console.log(e)
      console.log("No hay sesión")
      this.validUser = false
      if ( urlLogin ){
        return of(true)
      }else{
        router.navigate(["/login"])
        return of(false)
      }
    }))
  }


  /**
   * Devuelve un valor booleano observable cuyo valor es true si el 
   * usuario está en una partida y false si no lo está. Se consulta
   * la base de datos para saber esta información. Se presupone que 
   * esta función se llama solo si el usuario no ha cargado ya una partida.
   * 
   * Si el usuario está en partida, entonces navega automáticamente a la pantalla de
   * partida. Si no, navega a la pantalla de home
   * 
   * @param router 
   */
  public checkLastMatch(router: Router, gameService: GameService, wsService: WsService): Observable<boolean> {
    return this.http.get(UserService.baseUrl + "/session", UserService.httpOptions).pipe(
      map(response => {
        wsService._connect()
        this.updateUserData(response.body)
        if ( this.partida != null ){
          console.log("Partida no nula")
          console.log(this.partida)
          gameService.recargarPartida(this.partida)
          return true

        }else if ( this.bloqued != null ){
          console.log("Cuenta bloqueada!")
          router.navigate(["/banned"])
          return false

        }else{
          router.navigate(["/home"])
          return false
        }
      }
    ), catchError((e) => {
        console.log("EXCEPCION-------")
        console.log(e)
        console.log("No hay sesión")
        router.navigate(["/login"])
        return of(false)
    }))
  }

  /**
   * Checkea si el usuario puede acceder a la página de baneo
   * 
   * @param router 
   * @param gameService 
   * @param wsService 
   */
  public checkBan(router: Router, gameService: GameService, wsService: WsService): Observable<boolean> {
    return this.http.get(UserService.baseUrl + "/session", UserService.httpOptions).pipe(
      map(response => {
        this.updateUserData(response.body)
        wsService._connect()
        if ( this.partida != null ){
          console.log("Partida no nula")
          console.log(this.partida)
          gameService.recargarPartida(this.partida)
          router.navigate(["/board"])
          return false

        }else if ( this.bloqued != null ){
          console.log("Cuenta bloqueada!")
          router.navigate(["/banned"])
          return true

        }else{
          router.navigate(["/home"])
          return false
        }
      }
    ), catchError((e) => {
        console.log("EXCEPCION-------")
        console.log(e)
        console.log("No hay sesión")
        router.navigate(["/login"])
        return of(false)
    }))
  }

  public ObtenerEstadisticasJugador(){
    return this.http.get(UserService.stats + "/" + this.getUsername(), UserService.httpOptions)
  }

  public async CambiarContraseña(oldPassword: String, newPassword: String){
    let msg = {
      userId: this.getUsername(),
      oldPassw: oldPassword,
      newPassw: newPassword
    }
    console.log(msg)
    let response = await this.http.put(UserService.changePass, msg, UserService.httpOptions).toPromise()
    console.log(response)

    if(this.langService.selectedLang == "ESP"){
      if(response["body"]["nombre"] != null){
        this.snackBar.open("Contraseña cambiada correctamente" , "OK");
      }else{
        this.snackBar.open("La contraseña anterior no es correcta" , "OK");
      }
    }else{
      if(response["body"]["nombre"] != null){
        this.snackBar.open("Password changed correctly" , "OK");
      }else{
        this.snackBar.open("Incorrect password" , "OK");
      }
    }
  }

  public async updateUserAvatar(nuevoAvatar: String){
    let msg = {
      nombre: this.getUsername(),
      avatar: nuevoAvatar
    }
    let response = await this.http.put(UserService.update, msg, UserService.httpOptions).toPromise()
    console.log(response)
  }

  public async changeUserLanguage(){
    var language;
    if(this.langService.selectedLang == "ESP"){
      language = "Español";
    }else{
      language = "English";
    }
    let msg = {
      nombre: this.getUsername(),
      idioma: language
    }
    let response = await this.http.put(UserService.update, msg, UserService.httpOptions).toPromise()
    console.log(response)
  }
}



