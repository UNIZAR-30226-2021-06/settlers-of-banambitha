import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';
import { ShopService } from '../../../service/shop/shop.service'

@Component({
  selector: 'app-change-avatar',
  templateUrl: './change-avatar.component.html',
  styleUrls: ['./change-avatar.component.sass']
})
export class ChangeAvatarComponent implements OnInit {


  public empty: Boolean = false

  public profile_products: Array<Object> = []

  constructor(private shopService: ShopService, public userService: UserService, public langService: LangService) { }


  public updateView(){
    this.profile_products = []
    this.profile_products.push(new Object({url: "user_profile_image_original.png", nombre: "Original", precio: 0, tipo: "AVATAR", adquirido: true}))
    this.shopService.ObtenerProductosDisponibles().subscribe( response => {
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "AVATAR" && response.body[x]["adquirido"]){ 
          this.profile_products.push(response.body[x])
        }
      }
      this.empty =  this.profile_products.length == 0
    })
    console.log(this.profile_products)
  }

  

  ngOnInit(): void {
    this.updateView()
  }

  set(profile: String){
    console.log("set " + profile);
    ( async () => {
      try {
        this.userService.setAvatar(profile);
        this.userService.updateUserAvatar(profile);
        this.updateView()
      } catch( e ){
        console.log("no se pudo seleccionar el avatar")
      }
    })()
  }

}
