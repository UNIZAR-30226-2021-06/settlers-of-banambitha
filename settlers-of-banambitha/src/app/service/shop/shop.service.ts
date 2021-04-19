import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { UserService } from '../user/user.service';

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  private static readonly baseUrl = "http://localhost:8080/producto"
  private static readonly adquiridos = ShopService.baseUrl + "/adquiridos"
  private static readonly adquirir = ShopService.baseUrl + "/adquirir"
  private static readonly disponibles = ShopService.baseUrl + "/disponibles"
  private static readonly httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' }),
    withCredentials: true,
    observe: 'response' as 'response'
  }

  constructor( private http: HttpClient, private UserService: UserService ) { }

  public ObtenerProductosDisponibles(){
    return this.http.get(ShopService.disponibles + "/" + UserService.getUsername(), ShopService.httpOptions)
  }

  public ObtenerProductosAdquiridos(){
    return this.http.get(ShopService.adquiridos + "/" + UserService.getUsername(), ShopService.httpOptions)
  }

  public async adquirproducto(id_producto: String){
    let msg = {
      usuario_id: UserService.getUsername(),
      producto_id: id_producto
    }
    let response = await this.http.put(ShopService.adquirir, msg, ShopService.httpOptions).toPromise()
    UserService.updateUserData(response.body["usuario"])
  }
}
