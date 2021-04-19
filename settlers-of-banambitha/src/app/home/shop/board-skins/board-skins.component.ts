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

  buy(profile: String){
    console.log("comprar " + profile)
  }

  ngOnInit(): void {
    this.ShopService.ObtenerProductosDisponibles().subscribe( response => {
      console.log("fin")
      console.log(response)
      console.log(response.body)
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "APARIENCIA" && !response.body[x]["adquirido"]){ 
          console.log(response.body[x])
          this.board_products.push(response.body[x])
        }
      }
      this.empty =  this.board_products.length == 0
    })
  }

}
