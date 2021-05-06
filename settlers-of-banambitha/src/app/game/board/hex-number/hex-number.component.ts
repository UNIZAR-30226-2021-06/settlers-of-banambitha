import { Component, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { GameService, TipoTerreno } from 'src/app/service/game/game.service';

@Component({
  selector: 'board-hex-number',
  templateUrl: './hex-number.component.html',
  styleUrls: ['../board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HexNumberComponent implements OnInit {

  @Input() hexagono

  public numberNames: Array<String> = ["", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"]

  constructor(public gameService:GameService) { }

  ngOnInit(): void {
  }
  
  public tipoTerreno(): typeof TipoTerreno {
    return TipoTerreno
  }

  public move(): void {
    this.gameService.partida.tablero.hexagonos.ladron = this.hexagono
  }

}
