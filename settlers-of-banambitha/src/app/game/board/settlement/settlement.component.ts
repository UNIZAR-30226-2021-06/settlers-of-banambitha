import { Component, Input, OnInit, setTestabilityGetter, ViewEncapsulation } from '@angular/core';
import { GameService, TipoAsentamiento, TipoTerreno } from 'src/app/service/game/game.service';
import { BoardComponent } from '../board.component';

@Component({
  selector: 'board-settlement',
  templateUrl: './settlement.component.html',
  styleUrls: ['../board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SettlementComponent implements OnInit {
  public static readonly staticCSSPoblado: String = "house"
  public static readonly staticCSSCiudad:  String = "city"
  
  @Input() vertice: number
  @Input() posicion: String

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


  public readonly settlementColorMapping: Map<TipoAsentamiento, String> = SettlementComponent.staticSettlementColorMapping
  public readonly settlementTypeMapping:  Map<TipoAsentamiento, String> = SettlementComponent.staticSettlementTypeMapping
  public readonly settlementProp:         Map<TipoAsentamiento, number> = SettlementComponent.staticSettlementProp
  public readonly CSSPoblado: String = SettlementComponent.staticCSSPoblado
  public readonly CSSCiudad:  String = SettlementComponent.staticCSSCiudad

  constructor(public gameService: GameService) { }

  ngOnInit(): void {
  }

  public tipoTerreno(): typeof TipoTerreno {
    return TipoTerreno
  }

  public tipoAsentamiento(): typeof TipoAsentamiento {
    return TipoAsentamiento
  }


  public func(): void {

  }

  public build(): void{
    console.log("Construido poblado")
  }

  public improve(): void{
    console.log("Mejorar poblado")
  }


}
