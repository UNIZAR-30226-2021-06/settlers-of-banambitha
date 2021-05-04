import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GameService, TipoAsentamiento, TipoTerreno } from 'src/app/service/game/game.service';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  public readonly player1Color: String = "#056396"
  public readonly player2Color: String = "#96054b"
  public readonly player3Color: String = "#d2eb34"
  public readonly player4Color: String = "#49a607"
  public readonly CSSPoblado: String = "house"
  public readonly CSSCiudad:  String = "city"

  public readonly hexMapping: Map<TipoTerreno, String> = new Map<TipoTerreno,String>([
   [TipoTerreno.BOSQUE,   "wood"], 
   [TipoTerreno.CERRO ,   "coal"], 
   [TipoTerreno.DESIERTO, "sand"], 
   [TipoTerreno.MONTANYA, "brick"], 
   [TipoTerreno.PASTO,    "sheep"], 
   [TipoTerreno.SEMBRADO, "wheat"], 
  ]
  );

  public readonly settlementColorMapping: Map<TipoAsentamiento, String> = new Map<TipoAsentamiento, String>([
    [TipoAsentamiento.POBLADO_PLAYER_1, this.player1Color],
    [TipoAsentamiento.POBLADO_PLAYER_2, this.player2Color],
    [TipoAsentamiento.POBLADO_PLAYER_3, this.player3Color],
    [TipoAsentamiento.POBLADO_PLAYER_4, this.player4Color],
    [TipoAsentamiento.CIUDAD_PLAYER_1, this.player1Color],
    [TipoAsentamiento.CIUDAD_PLAYER_2, this.player2Color],
    [TipoAsentamiento.CIUDAD_PLAYER_3, this.player3Color],
    [TipoAsentamiento.CIUDAD_PLAYER_4, this.player4Color]
  ]);

  public readonly settlementTipeMapping: Map<TipoAsentamiento, String> = new Map<TipoAsentamiento, String>([
    [TipoAsentamiento.POBLADO_PLAYER_1, this.CSSPoblado],
    [TipoAsentamiento.POBLADO_PLAYER_2, this.CSSPoblado],
    [TipoAsentamiento.POBLADO_PLAYER_3, this.CSSPoblado],
    [TipoAsentamiento.POBLADO_PLAYER_4, this.CSSPoblado],
    [TipoAsentamiento.CIUDAD_PLAYER_1, this.CSSCiudad],
    [TipoAsentamiento.CIUDAD_PLAYER_2, this.CSSCiudad],
    [TipoAsentamiento.CIUDAD_PLAYER_3, this.CSSCiudad],
    [TipoAsentamiento.CIUDAD_PLAYER_4, this.CSSCiudad]
  ]);

  public readonly settlementProp: Map<TipoAsentamiento, number> = new Map<TipoAsentamiento, number>([
    [TipoAsentamiento.POBLADO_PLAYER_1, 1],
    [TipoAsentamiento.POBLADO_PLAYER_2, 2],
    [TipoAsentamiento.POBLADO_PLAYER_3, 3],
    [TipoAsentamiento.POBLADO_PLAYER_4, 4],
    [TipoAsentamiento.CIUDAD_PLAYER_1, 1],
    [TipoAsentamiento.CIUDAD_PLAYER_2, 2],
    [TipoAsentamiento.CIUDAD_PLAYER_3, 3],
    [TipoAsentamiento.CIUDAD_PLAYER_4, 4]
  ]);

  public numberNames: Array<String> = ["", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", "ten", "eleven", "twelve"]

  constructor(public dialog: MatDialog, public gameService: GameService) { }

  ngOnInit(): void {
  }

  openSettingsDialog() {
    this.dialog.open(SettingsDialog);

  }

  public tipoTerreno(): typeof TipoTerreno {
    return TipoTerreno
  }

  public tipoAsentamiento(): typeof TipoAsentamiento {
    return TipoAsentamiento
  }

  poblar() {
    this.gameService.partida.tablero.vertices.asentamiento[5] = TipoAsentamiento.POBLADO_PLAYER_1
  }

  ciudar(){
    this.gameService.partida.tablero.vertices.asentamiento[5] = TipoAsentamiento.POBLADO_PLAYER_2
  }


}


@Component({
  selector: 'settings-dialog',
  templateUrl: 'settings-dialog.html',
  styleUrls: ['settings-dialog.sass']
})
export class SettingsDialog {



  constructor(@Inject(MAT_DIALOG_DATA) public data: SettingsDialog, private router:Router, public gameService: GameService) {
    
  }

  goHome() {
    this.router.navigate(['/home/play'])
  }


}