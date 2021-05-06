import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { GameService } from 'src/app/service/game/game.service';

@Component({
  selector: 'board-harbor',
  templateUrl: './harbor.component.html',
  styleUrls: ['../board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HarborComponent implements OnInit {

  @Input() arista
  @Input() posicion

  constructor(public gameService: GameService) { }

  ngOnInit(): void {
  }

}
