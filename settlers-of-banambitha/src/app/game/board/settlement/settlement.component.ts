import { Component, Input, OnInit, setTestabilityGetter, ViewEncapsulation } from '@angular/core';
import { GameService, TipoAsentamiento, TipoTerreno } from 'src/app/service/game/game.service';
import { LangService } from 'src/app/service/lang/lang.service';
import { BoardComponent } from '../board.component';

@Component({
  selector: 'board-settlement',
  templateUrl: './settlement.component.html',
  styleUrls: ['../board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
/**
 * Clase que representa un asentamiento en el tablero
 */
export class SettlementComponent implements OnInit {
  public static readonly staticCSSPoblado: String = "house"
  public static readonly staticCSSCiudad:  String = "city"
  
  @Input() vertice: number   //vértice del asentamiento
  @Input() posicion: String  //posición del asentamiento dentro de su hexágono

  //Map con información del color que se le debe aplicar a cada asentamiento según su propietario
  public static readonly staticSettlementColorMapping: Map<TipoAsentamiento, String> = new Map<TipoAsentamiento, String>([
    [TipoAsentamiento.POBLADO_PLAYER_1, BoardComponent.player1Color],
    [TipoAsentamiento.POBLADO_PLAYER_2, BoardComponent.player2Color],
    [TipoAsentamiento.POBLADO_PLAYER_3, BoardComponent.player3Color],
    [TipoAsentamiento.POBLADO_PLAYER_4, BoardComponent.player4Color],
    [TipoAsentamiento.CIUDAD_PLAYER_1, BoardComponent.player1Color],
    [TipoAsentamiento.CIUDAD_PLAYER_2, BoardComponent.player2Color],
    [TipoAsentamiento.CIUDAD_PLAYER_3, BoardComponent.player3Color],
    [TipoAsentamiento.CIUDAD_PLAYER_4, BoardComponent.player4Color]
  ]);

  //Map con información de la clase css que corresponde a cada tipo de asentamiento
  public static readonly staticSettlementTypeMapping: Map<TipoAsentamiento, String> = new Map<TipoAsentamiento, String>([
    [TipoAsentamiento.POBLADO_PLAYER_1, SettlementComponent.staticCSSPoblado],
    [TipoAsentamiento.POBLADO_PLAYER_2, SettlementComponent.staticCSSPoblado],
    [TipoAsentamiento.POBLADO_PLAYER_3, SettlementComponent.staticCSSPoblado],
    [TipoAsentamiento.POBLADO_PLAYER_4, SettlementComponent.staticCSSPoblado],
    [TipoAsentamiento.CIUDAD_PLAYER_1, SettlementComponent.staticCSSCiudad],
    [TipoAsentamiento.CIUDAD_PLAYER_2, SettlementComponent.staticCSSCiudad],
    [TipoAsentamiento.CIUDAD_PLAYER_3, SettlementComponent.staticCSSCiudad],
    [TipoAsentamiento.CIUDAD_PLAYER_4, SettlementComponent.staticCSSCiudad]
  ]);

  //Map con información del id de jugador al que pertenece cada tipo de asentamiento
  public static readonly staticSettlementProp: Map<TipoAsentamiento, number> = new Map<TipoAsentamiento, number>([
    [TipoAsentamiento.POBLADO_PLAYER_1, 1],
    [TipoAsentamiento.POBLADO_PLAYER_2, 2],
    [TipoAsentamiento.POBLADO_PLAYER_3, 3],
    [TipoAsentamiento.POBLADO_PLAYER_4, 4],
    [TipoAsentamiento.CIUDAD_PLAYER_1, 1],
    [TipoAsentamiento.CIUDAD_PLAYER_2, 2],
    [TipoAsentamiento.CIUDAD_PLAYER_3, 3],
    [TipoAsentamiento.CIUDAD_PLAYER_4, 4]
  ]);


  //Referencias no estáticas a los maps estáticos anteriores. Se evita tener múltiples
  //copias de cada map y se puede hacer interpolación con estos.
  public readonly settlementColorMapping: Map<TipoAsentamiento, String> = SettlementComponent.staticSettlementColorMapping
  public readonly settlementTypeMapping:  Map<TipoAsentamiento, String> = SettlementComponent.staticSettlementTypeMapping
  public readonly settlementProp:         Map<TipoAsentamiento, number> = SettlementComponent.staticSettlementProp
  public readonly CSSPoblado: String = SettlementComponent.staticCSSPoblado
  public readonly CSSCiudad:  String = SettlementComponent.staticCSSCiudad

  /**
   * Constructor
   * 
   * @param gameService servicio de juego
   */
  constructor(public gameService: GameService, public langService: LangService) { }


  /**
   * Inicializador
   */
  ngOnInit(): void {
  }


  /**
   * Devuelve el tipo TipoTerreno
   * 
   * @return TipoTerreno
   */
  public tipoTerreno(): typeof TipoTerreno {
    return TipoTerreno
  }


  /**
   * Devuelve el tipo TipoAsentamiento
   * 
   * @return TipoAsentamiento
   */
  public tipoAsentamiento(): typeof TipoAsentamiento {
    return TipoAsentamiento
  }


  /**
   * Construye un poblado en el vértice (solo envía la jugada)
   */
  public build(): void{
    console.log("Construido poblado")
    if ( this.gameService.partida.jugadores[this.gameService.partida.miTurno - 1].primerosAsentamientos ){
      this.gameService.construirPoblado(this.vertice)
    }else{
      this.gameService.construirPrimerPoblado(this.vertice)
    }
  }

  /**
   * Mejora el poblado del vértice (solo envía la jugada)
   */ 
  public improve(): void{
    console.log("Mejorar poblado")
    this.gameService.mejorarPueblo(this.vertice)
  }


}
