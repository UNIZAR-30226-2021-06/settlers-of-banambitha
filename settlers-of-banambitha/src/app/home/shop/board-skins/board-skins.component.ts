import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-board-skins',
  templateUrl: './board-skins.component.html',
  styleUrls: ['./board-skins.component.sass']
})
export class BoardSkinsComponent implements OnInit {

  constructor() { }

  isActive1: boolean = false
  isActive2: boolean = false

  imageNames: { [src: string]: string} = {
    'Hardware' : "hardware_shop_image",
    'Espacial' : "espacial_shop_image",
  }

  getRandomInt() {
    return Math.floor(Math.random() * 1000000);
  }

  ngOnInit(): void {
  }

}
