import { Component, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { catchError } from 'rxjs/operators';
import { LangService } from 'src/app/service/lang/lang.service';
import { RoomService } from 'src/app/service/room/room.service';
import { SocialService, Usuario } from 'src/app/service/social/social.service';
import { UserService } from 'src/app/service/user/user.service';
import {FriendCardComponent} from './friend-card/friend-card.component';

@Component({
  selector: 'app-social-bar',
  templateUrl: './social-bar.component.html',
  styleUrls: ['./social-bar.component.sass']
})
export class SocialBarComponent implements OnInit {

  public value: String = "" 
  public errorBusqueda: boolean = false
  public lastSearchedUser : Usuario = null
  public sent = false
 
  constructor(public langService: LangService, public userService: UserService, public socialService: SocialService, public roomService: RoomService) {
    this.lastSearchedUser = {
      username: null, 
      avatar: null, 
      rachaActual: 0, 
      mayorRacha: 0, 
      numPartidas: 0, 
      porcentajeVictorias: 0, 
      totalVictorias: 0
    }
  }

  
  ngOnInit(): void {
  }

  findPlayer(): void {
    console.log(this.value);
    let that = this
    this.userService.findUserObservable(this.value).subscribe( (resp) => {
      this.errorBusqueda = false
      that.lastSearchedUser.username = resp["nombre"]
      that.lastSearchedUser.avatar   = resp["avatar"]
      that.userService.getUserStatsObservable(this.lastSearchedUser.username).subscribe (
        (stats) => {
          that.socialService.updateUserStats(stats, that.lastSearchedUser)
          that.sent = false
        }
      )
    }, (e) => {
      this.errorBusqueda = true
    })
  }

  enviarPeticion(): void {
    this.sent = true
    this.socialService.enviarPeticionAmistad(this.lastSearchedUser.username)
  }

  public sendMessage(user:String, message: any): void{
    this.socialService.enviarMensajePrivado(user, message.value)
    message.value = ""
    this.scrollDown(user as string)
  }

  public scrollDown(id: string): void {
    let objDiv = document.getElementById(id)
    if ( objDiv != null ){
      objDiv.scrollTop = objDiv.scrollHeight + 1
    }
  }

}
