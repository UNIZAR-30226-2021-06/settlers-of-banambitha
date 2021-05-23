import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { GameService } from '../service/game/game.service';
import { RoomService } from '../service/room/room.service';
import { UserService } from '../service/user/user.service';
import { WsService } from '../service/ws/ws.service';

@Injectable({
  providedIn: 'root'
})
export class AuthHomeGuard implements CanActivate {

  constructor(private router: Router, private userService: UserService, private gameService: GameService, private wsService: WsService, private roomService: RoomService){}
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      if ( this.userService.logedUser() ){
        return true
      }else{ 
        return this.userService.checkSession(this.router, false, this.gameService, this.wsService, this.roomService)
      }
  }
  
}
