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


  public percentageVictory : String;
  public games : String;
  public victories : String;
  public currentStreak : String;
  public bestStreak : String;
  constructor(public userService: UserService, private shopService: ShopService, public langService: LangService) { }

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
    this.percentageVictory = this.langService.get("victory-percentage");
    this.games = this.langService.get("games");
    this.victories = this.langService.get("victories");
    this.currentStreak = this.langService.get("current-streak");
    this.bestStreak = this.langService.get("best-streak");
    this.updateView();
  }

}
