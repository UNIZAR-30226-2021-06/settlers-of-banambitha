import { Component, OnInit , Input, Output} from '@angular/core';
import { SocialBarComponent } from './../social-bar.component';

@Component({
  selector: 'app-friend-browser',
  templateUrl: './friend-browser.component.html',
  styleUrls: ['./friend-browser.component.sass']
})
export class FriendBrowserComponent implements OnInit {


  constructor() { }

  ngOnInit(): void {
  }

  buscarPalabra(palabraBuscada:string){
  }

}
