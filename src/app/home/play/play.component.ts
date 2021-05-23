import { Component, NgModule, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameService } from 'src/app/service/game/game.service';
import { LangService } from 'src/app/service/lang/lang.service';
import { RoomService } from 'src/app/service/room/room.service';
import { ShopService } from 'src/app/service/shop/shop.service';
import { BoardSkin, UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-home-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.sass'],
})



export class PlayComponent implements OnInit {

  public availableSkins: Array<BoardSkin> = []
  public selectedSkin: BoardSkin 

  constructor(private router:Router, public UserService: UserService,
              public roomService: RoomService, public gameService: GameService,
              public langService: LangService, public shopService: ShopService) {

    this.formatLabelSelectedSkin    = this.formatLabelSelectedSkin.bind(this);
  }

  ngOnInit(): void {
    this.updateView()
  }

  change(event)
  {
    console.log(event)
    console.log(event.source.value)
    this.UserService.updateApariencia(event.source.value)
  }

  public updateView(){
    this.selectedSkin = this.UserService.apariencia
    this.shopService.ObtenerProductosDisponibles().subscribe( response => {
      this.availableSkins = [BoardSkin.CLASICA]
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "APARIENCIA" && response.body[x]["adquirido"]){ 
          this.availableSkins.push(this.UserService.getSkinFromUrl( response.body[x]["url"]) )
        }
      }
    })
  }

  public formatLabelSelectedSkin(value: BoardSkin): BoardSkin {
    this.selectedSkin = value;
    return value;
  }
}
