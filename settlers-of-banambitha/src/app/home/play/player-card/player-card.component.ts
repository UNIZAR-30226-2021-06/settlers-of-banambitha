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
  number_icons = ["looks_one", "looks_two", "looks_3", "looks_4"]
  player_color = ["red", "blue", "green", "grey"]

  isActive = false 

  constructor() { }

  ngOnInit(): void {
  }

}
