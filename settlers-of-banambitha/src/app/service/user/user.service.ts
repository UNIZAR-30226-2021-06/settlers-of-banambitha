import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, throwError } from 'rxjs';
import { catchError, retry } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  baseUrl = "http://localhost:8080/usuario"

  constructor(private http: HttpClient) {}

  some: object

  async getAllUsers(){
    let response:object
    await this.http.get("http://localhost:8080/usuario/all").toPromise().then( res => { response = res})
    return response[0]["nombre"]
  }

  register(name: String, mail: String, pass: String){
    let msg = {
      "nombre" : name,
      "mail" : mail,
      "contrasenya" : pass
    }
    return this.http.post(this.baseUrl + "/add", msg )
  }

  async validate(name: String, pass: String){
    let msg = {
      "nombre" : name,
      "contrasenya" : pass
    }
    return await this.http.post(this.baseUrl + "/validate", msg ).toPromise()
  }
}
