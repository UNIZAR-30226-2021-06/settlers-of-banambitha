import { Component, OnInit } from '@angular/core';
import {FriendCardComponent} from './friend-card/friend-card.component';

@Component({
  selector: 'app-social-bar',
  templateUrl: './social-bar.component.html',
  styleUrls: ['./social-bar.component.sass']
})
export class SocialBarComponent implements OnInit {

  public friends: Friend[];
  public filterFriends: Friend[];
 
  constructor() { 

    this.friends = [
      new Friend("adrian_1424", "456", "../../../../assets/images/shop/user/user_profile_image_1.png"),
      new Friend("fer8902", "234", "../../../../assets/images/shop/user/user_profile_image_2.png"),
      new Friend("javi__2000", "345", "../../../../assets/images/shop/user/user_profile_image_11.png"),
      new Friend("78_manuel", "123", "../../../../assets/images/shop/user/user_profile_image_10.png"),
      new Friend("alba_4090", "554", "../../../../assets/images/shop/user/user_profile_image_5.png"),
      new Friend("maria_player", "865", "../../../../assets/images/shop/user/user_profile_image_2.png"),
      new Friend("best_player12", "665", "../../../../assets/images/shop/user/user_profile_image_1.png"),
      new Friend("jaime_2020", "352", "../../../../assets/images/shop/user/user_profile_image_5.png"),
      new Friend("catan_player32", "544", "../../../../assets/images/shop/user/user_profile_image_1.png"),
      new Friend("8040_dani", "79", "../../../../assets/images/shop/user/user_profile_image_2.png"),
      new Friend("davidgarcia20", "45", "../../../../assets/images/shop/user/user_profile_image_11.png"),
      new Friend("jugador_catan10", "233", "../../../../assets/images/shop/user/user_profile_image_1.png"),
      new Friend("abcd_10", "546", "../../../../assets/images/shop/user/user_profile_image_10.png")
    ]

    this.filterFriends = this.friends;

  }
  
  ngOnInit(): void {

    
  }

  findFriends(): void{
    
  }

}

export class Friend{
  name: string;
  elo: string;
  profilePic: string;
  
  constructor(nombre:string, eloNum:string, picture:string){
    this.name = nombre;
    this.elo = eloNum;
    this.profilePic = picture;
  }
}
