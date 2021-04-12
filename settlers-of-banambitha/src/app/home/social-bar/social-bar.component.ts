import { Component, OnInit } from '@angular/core';
import {FriendCardComponent} from './friend-card/friend-card.component';

@Component({
  selector: 'app-social-bar',
  templateUrl: './social-bar.component.html',
  styleUrls: ['./social-bar.component.sass']
})
export class SocialBarComponent implements OnInit {

  public friends: string[];
 
  constructor() { 

    this.friends = [
      "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"
    ]

  }
  
  ngOnInit(): void {

    
  }

}
