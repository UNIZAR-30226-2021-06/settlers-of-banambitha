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
  private username: String
  private mail: String
  private idioma: String
  private avatar: String
  private apariencia: String
  private saldo: Number
  private validUser: boolean = false

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

  logedUsed(): Boolean {
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
    this.username = response["nombre"]
    this.apariencia = response["apariencia"]
    this.saldo = response["saldo"]
    this.mail = response["mail"]
    this.avatar = response["avatar"]
    this.validUser = true
  }


  /**
   * Si el usuario existe en el sistema entonces lanza una excepción, en caso 
   * contrario no hace nada.  
   * @param name 
   */
  async findUser(name: String){
    let found: boolean = false
    try {
      let response = await this.http.get(this.baseUrl + "/find/" + name).toPromise()
      found = true
    } catch(e){}
    if (found){
      throw new Error("El nombre de usuario ya existe")
    }
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
    this.username = response["nombre"]
    this.apariencia = response["apariencia"]
    this.saldo = response["saldo"]
    this.mail = response["mail"]
    this.avatar = response["avatar"]
    this.validUser = true
  }
}
