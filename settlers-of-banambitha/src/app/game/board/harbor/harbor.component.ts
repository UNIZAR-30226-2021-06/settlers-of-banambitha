import { Component, Inject, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import { GameService, Recurso, TipoPuerto } from 'src/app/service/game/game.service';
import { BoardComponent } from '../board.component';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'board-harbor',
  templateUrl: './harbor.component.html',
  styleUrls: ['../board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class HarborComponent implements OnInit {

  @Input() arista
  @Input() posicion

  public tipoPuerto: TipoPuerto
  public recursosRequeridos: number
  public tipoPuertoString: string
  
  public readonly playerColors: Array<String> = [BoardComponent.player1Color,
                                                 BoardComponent.player2Color,
                                                 BoardComponent.player3Color,
                                                 BoardComponent.player4Color]


  constructor(public dialog: MatDialog, public gameService: GameService) { }

  public ngOnInit(): void {
    this.tipoPuertoString = this.getPortInfo();
  }

  public openExternalTradeDialog(): void {
    let nuevosRecursos: number = this.recursosRequeridos
    if ( this.gameService.partida.tablero.aristas.camino[this.arista] == this.gameService.miTipoCamino() ){
      nuevosRecursos--
    }

    this.dialog.open(ExternalTradeDialog, { data: { requeridos: nuevosRecursos, tipo: this.tipoPuerto, nombre: this.tipoPuertoString}})
  }

  private getPortInfo(): string{

    this.tipoPuerto = this.gameService.TipoPuerto(this.arista)
    let puertoString: string

    switch ( this.tipoPuerto ){
      case TipoPuerto.MADERA:
        puertoString = "Puerto de madera"
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.ARCILLA:
        puertoString = "Puerto de arcilla"
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.LANA:
        puertoString = "Puerto de lana"
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.MINERAL:
        puertoString = "Puerto de minerales"
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.CEREAL:
        puertoString = "Puerto de cereales"
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.BASICO:
        puertoString = "Puerto básico"
        this.recursosRequeridos = 4
        break;

      default:
        return ""
    }

    return puertoString

  }


}

interface Material {
  value: Recurso;
  viewValue: string;
}


@Component({
  selector: 'app-trade-external',
  templateUrl: 'external-trade-dialog.html',
  styleUrls: ['./trade.dialog.sass']
})
export class ExternalTradeDialog {

  public tipoPuerto:         TipoPuerto
  public recursosRequeridos: number
  public nombrePuerto:       string
  public materialSolicitado: string
  public numMadera:  number = 0
  public numArcilla: number = 0
  public numCereal:  number = 0
  public numMineral: number = 0
  public numLana:    number = 0


  options: FormGroup;
  colorControl = new FormControl('primary');

  constructor(public dialogRef: MatDialogRef<ExternalTradeDialog>, @Inject(MAT_DIALOG_DATA) public data: any, fb: FormBuilder) {
    this.recursosRequeridos = data["requeridos"]
    this.tipoPuerto = data["tipo"]
    this.nombrePuerto = data["nombre"]
    this.formatLabelMadera  = this.formatLabelMadera.bind(this);
    this.formatLabelLana    = this.formatLabelLana.bind(this);
    this.formatLabelCereal  = this.formatLabelCereal.bind(this);
    this.formatLabelMineral = this.formatLabelMineral.bind(this);
    this.formatLabelArcilla = this.formatLabelArcilla.bind(this);
  }

   materials: Material[] = [
    { value: Recurso.MADERA, viewValue: 'Madera'},
    { value: Recurso.LANA, viewValue: 'Lana'},
    { value: Recurso.CEREAL, viewValue: 'Cereales'},
    { value: Recurso.ARCILLA, viewValue: 'Arcilla'},
    { value: Recurso.MINERAL, viewValue: 'Mineral'}
  ];

  onSubmit() {
    console.log(this.materialSolicitado)
  }

  public tiposPuerto(): typeof TipoPuerto{
    return TipoPuerto
  }

  formatLabelMadera(value: number) {
    this.numMadera = value;
    return value;
  }

  formatLabelArcilla(value: number) {
    this.numArcilla = value;
    return value;
  }

  formatLabelLana(value: number) {
    this.numLana = value;
    return value;
  }

  formatLabelMineral(value: number) {
    this.numMineral = value;
    return value;
  }

  formatLabelCereal(value: number) {
    this.numCereal = value;
    return value;
  }


}