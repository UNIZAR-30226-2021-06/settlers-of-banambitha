import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';
import { ShopService } from 'src/app/service/shop/shop.service';
import { UserService } from 'src/app/service/user/user.service';
import { BoardSkinsComponent } from '../board-skins/board-skins.component';

@Component({
  selector: 'app-profile-pictures',
  templateUrl: './profile-pictures.component.html',
  styleUrls: ['./profile-pictures.component.sass']
})
export class ProfilePicturesComponent implements OnInit {

  constructor(public userService: UserService, private shopService: ShopService, public langService: LangService) { }

  public empty: Boolean = false

  public profile_products: Array<Object> = []

  public updateView(){
    this.profile_products = []
    this.shopService.ObtenerProductosDisponibles().subscribe( response => {
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "AVATAR" && !response.body[x]["adquirido"]){ 
          this.profile_products.push(response.body[x])
        }
      }
      this.empty =  this.profile_products.length == 0
    })
  }

  buy(profile: String){
    console.log("comprar " + profile);
    ( async () => {
      try {
        await this.shopService.adquirproducto(profile)
        this.updateView()
      } catch( e ){
        console.log("no se pudo adquirir el producto")
      }
    })()
  }

  ngOnInit(): void {
    this.updateView()
  }

}
