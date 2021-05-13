import { Component, Inject, Input, OnInit, ViewEncapsulation } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import { GameService, Jugador, Recurso, TipoPuerto } from 'src/app/service/game/game.service';
import { BoardComponent } from '../board.component';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'board-harbor',
  templateUrl: './harbor.component.html',
  styleUrls: ['../board.component.scss'],
  encapsulation: ViewEncapsulation.None
})
/**
 * Clase que representa un puerto del tablero
 */
export class HarborComponent implements OnInit {

  @Input() arista    //Arista del puerto
  @Input() posicion  //Posición del puerto dentro del hexágono de agua

  public tipoPuerto:         TipoPuerto
  public recursosRequeridos: number
  public tipoPuertoString:   string
  
  //Map con información del color que se le asigna a cada jugador en la 
  //interfaz del juego 
  public readonly playerColors: Array<String> = [BoardComponent.player1Color,
                                                 BoardComponent.player2Color,
                                                 BoardComponent.player3Color,
                                                 BoardComponent.player4Color]


  /**
   * Consrtuctor
   * 
   * @param dialog 
   * @param gameService 
   */
  constructor(public dialog: MatDialog, public gameService: GameService, public langService: LangService) { }


  /**
   * Inicializador
   */
  public ngOnInit(): void {
    this.tipoPuertoString = this.getPortInfo();
  }


  /**
   * Abre el pop-up de comercio marítimo
   */
  public openExternalTradeDialog(): void {
    let nuevosRecursos: number = this.recursosRequeridos
    if ( this.gameService.partida.tablero.aristas.camino[this.arista] == this.gameService.miTipoCamino() ){
      nuevosRecursos--
    }

    this.dialog.open(ExternalTradeDialog, { data: { requeridos: nuevosRecursos, tipo: this.tipoPuerto, nombre: this.tipoPuertoString, arista: this.arista}})
  }


  /**
   * Obtiene la información implícita del puerto: material que ofrece
   * y cantidades de recursos que se intercambian. 
   */
  private getPortInfo(): string{

    this.tipoPuerto = this.gameService.TipoPuerto(this.arista)
    let puertoString: string

    switch ( this.tipoPuerto ){
      case TipoPuerto.MADERA:
        puertoString = this.langService.get("puerto-madera")
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.ARCILLA:
        puertoString = this.langService.get("puerto-arcilla")
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.LANA:
        puertoString = this.langService.get("puerto-lana")
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.MINERAL:
        puertoString = this.langService.get("puerto-mineral")
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.CEREAL:
        puertoString = this.langService.get("puerto-cereal")
        this.recursosRequeridos = 3
        break;

      case TipoPuerto.BASICO:
        puertoString = this.langService.get("puerto-basico")
        this.recursosRequeridos = 4
        break;

      default:
        return ""
    }

    return puertoString

  }
}


/**
 * Clase material para el selector de material de la
 * interfaz
 */
interface Material {
  value: Recurso;
  viewValue: string;
}


@Component({
  selector: 'app-trade-external',
  templateUrl: 'external-trade-dialog.html',
  styleUrls: ['./trade.dialog.sass']
})
/**
 * Clase que gestiona el pop-up de comercio marítimo
 */
export class ExternalTradeDialog {

  public tipoPuerto:         TipoPuerto  
  public me:                 Jugador     
  public recursosRequeridos: number      
  public nombrePuerto:       string
  public materialSolicitado: Recurso
  public numMadera:  number = 0
  public numArcilla: number = 0
  public numCereal:  number = 0
  public numMineral: number = 0
  public numLana:    number = 0

  private aristaPuerto: number


  options: FormGroup;
  colorControl = new FormControl('primary');

  materials: Material[] = [
    { value: Recurso.MADERA, viewValue: 'Madera'},
    { value: Recurso.LANA, viewValue: 'Lana'},
    { value: Recurso.CEREAL, viewValue: 'Cereales'},
    { value: Recurso.ARCILLA, viewValue: 'Arcilla'},
    { value: Recurso.MINERAL, viewValue: 'Mineral'}
  ];

  
  /**
   * Constructor. 
   * 
   * @param dialogRef 
   * @param data Contiene información relevante para el pop-up: requeridos (entero), 
   * tipo (tipoPuerto), nombre (string) y arista (entero)
   * @param fb 
   * @param gameService 
   */
  constructor(public dialogRef: MatDialogRef<ExternalTradeDialog>, @Inject(MAT_DIALOG_DATA) public data: any,
             fb: FormBuilder ,public gameService: GameService, public langService: LangService) {
    this.recursosRequeridos = data["requeridos"]
    this.tipoPuerto         = data["tipo"]
    this.nombrePuerto       = data["nombre"]
    this.aristaPuerto       = data["arista"]
    this.me = this.gameService.partida.jugadores[this.gameService.partida.miTurno - 1]
    this.checkRecursoPuerto()
    this.formatLabelMadera  = this.formatLabelMadera.bind(this);
    this.formatLabelLana    = this.formatLabelLana.bind(this);
    this.formatLabelCereal  = this.formatLabelCereal.bind(this);
    this.formatLabelMineral = this.formatLabelMineral.bind(this);
    this.formatLabelArcilla = this.formatLabelArcilla.bind(this);
  }


  /**
   * Envía la petición de comercio marítimo
   */
  public onSubmit() {
    this.gameService.
    comerciarConPuerto(this.aristaPuerto, this.materialSolicitado,
                       this.numMadera, this.numArcilla,
                       this.numMineral, this.numCereal, this.numLana)
  }


  /**
   * Comprueba cual es el recurso que se intercambia en el puerto 
   * (si es que es un puerto especializado). Actualiza el atributo 
   * this.materialSolicitado
   */
  public checkRecursoPuerto(): void{
    switch (this.tipoPuerto){
      case TipoPuerto.ARCILLA:
        this.materialSolicitado = Recurso.ARCILLA
        break

      case TipoPuerto.MADERA:
        this.materialSolicitado = Recurso.MADERA
        break

      case TipoPuerto.MINERAL:
        this.materialSolicitado = Recurso.MINERAL
        break

      case TipoPuerto.LANA:
        this.materialSolicitado = Recurso.LANA
        break

      case TipoPuerto.CEREAL:
        this.materialSolicitado = Recurso.CEREAL
        break

      default: 
        this.materialSolicitado = null
    }
  }

  public tiposPuerto(): typeof TipoPuerto{
    return TipoPuerto
  }

  public formatLabelMadera(value: number): number {
    this.numMadera = value;
    return value;
  }

  public formatLabelArcilla(value: number): number {
    this.numArcilla = value;
    return value;
  }

  public formatLabelLana(value: number): number {
    this.numLana = value;
    return value;
  }

  public formatLabelMineral(value: number): number {
    this.numMineral = value;
    return value;
  }

  public formatLabelCereal(value: number): number {
    this.numCereal = value;
    return value;
  }

  public min(x: number, y:number):number{
    return x < y ? x : y
  }

}