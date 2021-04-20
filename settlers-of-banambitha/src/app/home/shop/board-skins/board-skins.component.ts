import { Component, OnInit } from '@angular/core';
import { ShopService } from 'src/app/service/shop/shop.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-board-skins',
  templateUrl: './board-skins.component.html',
  styleUrls: ['./board-skins.component.sass']
})
export class BoardSkinsComponent implements OnInit {

  constructor(private UserService: UserService, private ShopService: ShopService) { }


  public empty: boolean = false
  public board_products: Array<Object> = []

  public updateView(){
    this.ShopService.ObtenerProductosDisponibles().subscribe( response => {
      this.board_products = []
      let saldo = UserService.getSaldo()
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "APARIENCIA" && !response.body[x]["adquirido"]){ 
          response.body[x]["available"] = saldo >= parseInt(response.body[x]["precio"], 10)
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
        await this.ShopService.adquirproducto(profile)
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
