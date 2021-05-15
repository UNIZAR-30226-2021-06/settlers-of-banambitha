import { Component, Input, OnInit } from '@angular/core';
import { GameService, TipoCamino } from 'src/app/service/game/game.service';
import { BoardComponent } from '../board.component';

@Component({
  selector: 'board-road',
  templateUrl: './road.component.html',
  styleUrls: ['../board.component.scss']
})
/**
 * Clase que representa un camino del tablero
 */
export class RoadComponent implements OnInit {

  @Input() arista   //número de la arista del camino
  @Input() posicion //posición del camino dentro de su hexágono

  //Map con información del color que se le debe aplicar a cada camino según su propietario
  public static readonly staticRoadColorMapping: Map<TipoCamino, String> = new Map<TipoCamino, String>([
    [TipoCamino.PLAYER_1, BoardComponent.player1Color],
    [TipoCamino.PLAYER_2, BoardComponent.player2Color],
    [TipoCamino.PLAYER_3, BoardComponent.player3Color],
    [TipoCamino.PLAYER_4, BoardComponent.player4Color]
  ]);

  //Referencia estática al Map anterior para poder utilizar interpolación
  public readonly roadColorMapping: Map<TipoCamino, String> = RoadComponent.staticRoadColorMapping

  /**
   * Constructor
   * 
   * @param gameService servicio de juego
   */
  constructor(public gameService: GameService) { }


  /**
   * Inicializador
   */
  ngOnInit(): void {
  }


  /**
   * Construye un camino en la arista dada (solo envía la jugada al servidor)
   */
  public build(): void {
    console.log("construir camnio")
    if ( this.gameService.partida.jugadores[this.gameService.partida.miTurno - 1].primerosCaminos ){
      this.gameService.construirCamino(this.arista)
    }else{
      this.gameService.construirPrimerCamino(this.arista)
    }
  }


  /**
   * Devuelve el tipo de dato TipoCamino
   * 
   * @return TipoCamino
   */
  public tipoCamino(): typeof TipoCamino{
    return TipoCamino
  }

}
