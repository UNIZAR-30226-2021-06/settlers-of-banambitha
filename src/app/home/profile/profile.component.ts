import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';
import { ShopService } from 'src/app/service/shop/shop.service';
import { UserService } from 'src/app/service/user/user.service';


@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.sass']
})
export class ProfileComponent implements OnInit {

  public stats: Array<Object> = []
  public username: String
  public mail: String
  public avatar: String

  constructor(private userService: UserService, private shopService: ShopService, public langService: LangService) { }

  public updateView(){
    this.username = this.userService.getUsername();
    console.log(this.username)
    this.mail = this.userService.getMail();
    console.log(this.mail)
    this.avatar = this.userService.getAvatar();
    console.log(this.avatar)
    this.userService.ObtenerEstadisticasJugador().subscribe( response => {
     this.stats.push(response.body["porcentajeDeVictorias"])
     this.stats.push(response.body["partidasJugadas"])
     this.stats.push(response.body["totalDeVictorias"])
     this.stats.push(response.body["rachaDeVictoriasActual"])
     this.stats.push(response.body["mayorRachaDeVictorias"])
     
    })
    console.log(this.stats)
  }

  ngOnInit(): void {
    this.updateView();
  }

}
