import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})

/**
 * Clase que implementa las peticiones al servidor relacionadas con el usuario, 
 * y almacena la información de sesión del usuario.
 */
export class UserService {

  private readonly baseUrl = "http://localhost:8080/usuario"
  private readonly addUrl = this.baseUrl + "/add"
  private readonly validae = this.baseUrl + "/validate"

  //Atributos relacionados con el usuario
  private static username: String
  private static mail: String
  private static idioma: String
  private static avatar: String
  private static apariencia: String
  private static saldo: Number
  private static validUser: boolean = false

  constructor(private http: HttpClient) {}

  //Getters
  public static getUsername(): String {
    return this.username;
  }

  public static getMail(): String {
    return this.mail;
  }

  public static getIdioma(): String {
    return this.idioma;
  }

  public static getAvatar(): String {
    return this.avatar;
  }

  public static getApariencia(): String {
    return this.apariencia;
  }

  public static getSaldo(): Number {
    return this.saldo;
  }

  public static logedUser(): Boolean {
    return this.validUser
  }

  /**
   * Trata de registrar un nuevo usuario en el sistema. Si se produce
   * algún error, lanza una excepción.
   * @param name nombre del usuario
   * @param mail mail del usuario
   * @param pass contraseña del usuario
   */
  async register(name: String, mail: String, pass: String){
    let msg = {
      "nombre" : name,
      "mail" : mail,
      "contrasenya" : pass
    }
    let response = await this.http.post(this.baseUrl + "/add", msg ).toPromise()
    UserService.username = response["nombre"]
    UserService.apariencia = response["apariencia"]
    UserService.saldo = response["saldo"]
    UserService.mail = response["mail"]
    UserService.avatar = response["avatar"]
    UserService.validUser = true
  }


  /**
   * Si el usuario existe en el sistema entonces lanza una excepción, en caso 
   * contrario no hace nada.  
   * @param name 
   */
  async findUser(name: String){
    let found: boolean = false
    let response: Object
    try {
      response = await this.http.get(this.baseUrl + "/find/" + name).toPromise()
      found = true
    } catch(e){}
    if (found){
      throw new Error("El nombre de usuario ya existe")
    }else{
      return response
    }
  }

  findUserObservable(name: String){
    return this.http.get(this.baseUrl + "/find/" + name)
  }


  /**
   * Trata de validar el usuario y contraseña. Si el usuario dado
   * tiene la conraseña dada, entonces actualiza los datos de usuario. 
   * En caso contrario lanza una excepción. 
   * @param name 
   * @param pass 
   */
  async validatePromise(name: String, pass: String){
    let msg = {
      "nombre" : name,
      "contrasenya" : pass
    }
    let response = await this.http.post(this.baseUrl + "/validate", msg ).toPromise()
    UserService.apariencia = response["apariencia"]
    UserService.saldo = response["saldo"]
    UserService.mail = response["mail"]
    UserService.avatar = response["avatar"]
    UserService.validUser = true
  }
}
