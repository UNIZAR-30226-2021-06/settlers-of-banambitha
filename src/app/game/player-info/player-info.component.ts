import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import {MatAccordion} from '@angular/material/expansion';
import { GameService, Jugador, Recurso } from 'src/app/service/game/game.service';
import { LangService } from 'src/app/service/lang/lang.service';
import { BoardComponent } from '../board/board.component';


@Component({
  selector: 'app-player-info',
  templateUrl: './player-info.component.html',
  styleUrls: ['./player-info.component.sass']
})
export class PlayerInfoComponent implements OnInit {


  public readonly playerColors: Array<String> = [BoardComponent.player1Color,
                                                 BoardComponent.player2Color,
                                                 BoardComponent.player3Color,
                                                 BoardComponent.player4Color]

  public MyMessage: string = ""

  constructor(public dialog: MatDialog, public gameService: GameService, public langService: LangService) { }

  public ngOnInit(): void {
  }

  public openInternalTradeDialog(playerId: number): void {
    this.dialog.open(InternalTradeDialog, { data: { player: playerId}})
  }

  public sendMessage(message: any): void{
    this.gameService.enviarMensaje(message.value)
    message.value = ""
    this.scrollDown()
  }

  public scrollDown(): void {
    let objDiv = document.getElementById("chatbox")
    objDiv.scrollTop = objDiv.scrollHeight + 1
  }

}


interface Material {
  value: string;
  viewValue: string;
}


@Component({
  selector: 'app-trade-internal',
  templateUrl: 'internal-trade-dialog.html',
  styleUrls: ['./trade.dialog.sass']
})
export class InternalTradeDialog implements OnInit {

  public offerPlayer:     Jugador;
  public offerMaterial:   string;
  public receiveMaterial: string;
  public ammountGiven:    number;
  public ammountReceived: number;

  constructor(public dialogRef: MatDialogRef<InternalTradeDialog>, public gameService: GameService, 
    @Inject(MAT_DIALOG_DATA) public data: any, public langService: LangService) {
    this.formatLabelOffer = this.formatLabelOffer.bind(this);
    this.formatLabelReceive = this.formatLabelReceive.bind(this);
    this.offerPlayer = this.gameService.partida.jugadores[data["player"]]
  }

   ngOnInit(): void {
  }

   materials: Material[] = [
    { value: 'madera', viewValue: 'Madera'},
    { value: 'lana', viewValue: 'Lana'},
    { value: 'cereales', viewValue: 'Cereales'},
    { value: 'arcilla', viewValue: 'Arcilla'},
    { value: 'mineral', viewValue: 'Mineral'}
  ];


  private static readonly materialRecurso: Map<string, Recurso> = new Map<string, Recurso>([
    ['madera', Recurso.MADERA],
    ['lana', Recurso.LANA], 
    ['cereales', Recurso.CEREAL],
    ['mineral', Recurso.MINERAL],
    ['arcilla', Recurso.ARCILLA]
  ])


  formatLabelOffer(value: number) {
    this.ammountGiven = value;
    return value;
  }

  formatLabelReceive(value: number) {
    this.ammountReceived = value;
    return value;
  }

  /**
   * Comerciar
   */
  onSubmit() {
    console.log(this.offerMaterial);  
    console.log(this.offerPlayer);  
    console.log(this.receiveMaterial);
    console.log(this.ammountGiven);
    console.log(this.ammountReceived);
    this.gameService.comerciarConJugador(this.offerPlayer.turno, InternalTradeDialog.materialRecurso.get(this.offerMaterial), 
                                         InternalTradeDialog.materialRecurso.get(this.receiveMaterial),
                                         this.ammountGiven, this.ammountReceived )
  }
}
