import { Component, Input, OnInit } from '@angular/core';
import { GameService, TipoCamino } from 'src/app/service/game/game.service';
import { BoardComponent } from '../board.component';

@Component({
  selector: 'board-road',
  templateUrl: './road.component.html',
  styleUrls: ['../board.component.scss']
})
export class RoadComponent implements OnInit {

  @Input() arista
  @Input() posicion

  public static readonly staticRoadColorMapping: Map<TipoCamino, String> = new Map<TipoCamino, String>([
    [TipoCamino.PLAYER_1, BoardComponent.player1Color],
    [TipoCamino.PLAYER_2, BoardComponent.player2Color],
    [TipoCamino.PLAYER_3, BoardComponent.player3Color],
    [TipoCamino.PLAYER_4, BoardComponent.player4Color]
  ]);

  public readonly roadColorMapping: Map<TipoCamino, String> = RoadComponent.staticRoadColorMapping

  constructor(public gameService: GameService) { }

  ngOnInit(): void {
  }


  public build(): void {
    this.gameService.partida.tablero.aristas.camino[this.arista] = TipoCamino.PLAYER_1
  }

  public tipoCamino(): typeof TipoCamino{
    return TipoCamino
  }

}
