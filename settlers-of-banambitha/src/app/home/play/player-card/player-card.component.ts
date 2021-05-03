import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-player-card',
  templateUrl: './player-card.component.html',
  styleUrls: ['./player-card.component.sass']
})
export class PlayerCardComponent implements OnInit {

  @Input() playerName: string
  @Input() playerELO: bigint
  @Input() playerNumber: number
  @Input() profilePictureUrl: string

  number_icons = ["looks_one", "looks_two", "looks_3", "looks_4"]
  player_color = ["white", "white", "white", "white"]

  isActive = false 

  constructor() { }

  ngOnInit(): void {
    if ( this.profilePictureUrl == null){
      this.profilePictureUrl = "/assets/images/shop/user/user_profile_image_original.png"
    }
  }

}
