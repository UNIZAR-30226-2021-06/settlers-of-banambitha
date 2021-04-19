import { Component, OnInit } from '@angular/core';
import { ShopService } from 'src/app/service/shop/shop.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-profile-pictures',
  templateUrl: './profile-pictures.component.html',
  styleUrls: ['./profile-pictures.component.sass']
})
export class ProfilePicturesComponent implements OnInit {

  constructor(private UserService: UserService, private ShopService: ShopService) { }

  imageNames : string [] = [
    'Dave',
    'Señor bigotes',
    'Científica',
    'Bruno',
    'Agente Secreto',
    'Desarrollador Dae',
    'Developer',
    'Ingeniera',
    'Azafata',
    'Alfred',
    'Profesora',
    'Señora formal',
    'Astronauta',
    'Anciano feliz',
    'Chico tranquilo',
    'barbero',
    'Hombre desconfiado'
  ]

  public empty: Boolean = false

  public profile_products: Array<Object> = []

  buy(profile: String){
    console.log("comprar " + profile)
  }

  ngOnInit(): void {
    this.ShopService.ObtenerProductosDisponibles().subscribe( response => {
      console.log("fin")
      console.log(response)
      console.log(response.body)
      for ( var x in response.body){
        if ( response.body[x]["tipo"]  == "AVATAR" && !response.body[x]["adquirido"]){ 
          console.log(response.body[x])
          this.profile_products.push(response.body[x])
        }
      }
      this.empty =  this.profile_products.length == 0
    })
  }

}
