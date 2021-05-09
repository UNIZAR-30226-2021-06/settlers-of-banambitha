import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, of, throwError } from 'rxjs';
import { catchError, retry, map } from 'rxjs/operators';
import { environment } from 'src/environments/environment';


@Injectable({
  providedIn: 'root'
})

/**
 * Clase que implementa las peticiones al servidor relacionadas con el usuario, 
 * y almacena la información de sesión del usuario.
 */
export class UserService {

  private static readonly baseUrl = environment.baseUrl + "/usuario"
  private static readonly addUrl  = UserService.baseUrl + "/add"
  private static readonly validae = UserService.baseUrl + "/validate"
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
  public apariencia: String
  public saldo:      Number
  public partida:    String
  public validUser:  boolean = false

  constructor(private http: HttpClient) {}

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

  public updateUserData(userData: Object){
    this.username = userData["nombre"]
    this.apariencia = userData["apariencia"]
    this.saldo = userData["saldo"]
    this.mail = userData["mail"]
    this.avatar = userData["avatar"]
    this.partida = userData["partida"]
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
   * Devuelve la promise de una petición que solicita la 
   * búsqueda de un usuario
   * @param name 
   */
  public findUserObservable(name: String){
    return this.http.get(UserService.baseUrl + "/find/" + name)
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
  public checkSession(router: Router, urlLogin: boolean): Observable<boolean>{
    return this.http.get(UserService.baseUrl + "/session", UserService.httpOptions).pipe(
      map(response => {
        this.updateUserData(response.body)
        if ( urlLogin ){
          router.navigate(["/home"])
          return false
        } else {
          return true
        }
      }
    ), catchError(() => {
      this.validUser = false
      if ( urlLogin ){
        return of(true)
      }else{
        router.navigate(["/login"])
        return of(false)
      }
    }))
  }
}
