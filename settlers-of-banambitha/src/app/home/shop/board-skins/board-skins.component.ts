import { Component, OnInit } from '@angular/core';
import { ShopService } from 'src/app/service/shop/shop.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-board-skins',
  templateUrl: './board-skins.component.html',
  styleUrls: ['./board-skins.component.sass']
})
export class BoardSkinsComponent implements OnInit {

  constructor(private userService: UserService, private shopService: ShopService) { }


  public empty: boolean = false
  public board_products: Array<Object> = []

  public updateView(){
    this.shopService.ObtenerProductosDisponibles().subscribe( response => {
      this.board_products = []
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "APARIENCIA" && !response.body[x]["adquirido"]){ 
          this.board_products.push(response.body[x])
        }
      }
      this.empty =  this.board_products.length == 0
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
