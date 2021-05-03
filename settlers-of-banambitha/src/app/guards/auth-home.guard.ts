import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserService } from '../service/user/user.service';

@Injectable({
  providedIn: 'root'
})
export class AuthHomeGuard implements CanActivate {

  constructor(private router: Router, private userService: UserService){}
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
      if ( this.userService.logedUser() ){
        return true
      }else{ 
        return this.userService.checkSession(this.router, false)
      }
  }
  
}
