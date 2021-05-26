import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { environment } from 'src/environments/environment';
import { GameService } from '../game/game.service';
import { UserService } from '../user/user.service';
import { Connectable, WsService } from '../ws/ws.service';

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

@Injectable({
  providedIn: 'root'
})
export class SocialService implements Connectable{

  private static readonly baseUrl:  String = environment.baseUrl   + "/usuario"

  /**
   * Constructor. Se suscribe a los topics necesarios para poder gestionar
   * toda la dinámica de salas e invitaciones a estas.
   * @param wsService servicio de websockets a utilzar (singleton)
   * @param router  router de la aplicación
   * @param userService servicio de usuario a utilizar (singleton)
   */
  constructor(private wsService: WsService, private router: Router, private userService: UserService, private gameService: GameService, private http: HttpClient) {
    if ( ! wsService.atatchConnectable(this)){
      this.onConnect();
    }
  }

  onConnect(): void {
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
