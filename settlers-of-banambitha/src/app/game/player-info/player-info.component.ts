import { Component, Inject, OnInit, ViewChild } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import {MatAccordion} from '@angular/material/expansion';
import { GameService, Jugador } from 'src/app/service/game/game.service';
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

  public Mymessage: string 

  constructor(public dialog: MatDialog, public gameService: GameService) { }

  ngOnInit(): void {
  }

  openInternalTradeDialog(playerId: number) {
    this.dialog.open(InternalTradeDialog, { data: { player: playerId}})
  }

  sendMessage(message: any){
    console.log("called")
    console.log(this.Mymessage)
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
    @Inject(MAT_DIALOG_DATA) public data: any) {
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


  formatLabelOffer(value: number) {
    this.ammountGiven = value;
    return value;
  }

  formatLabelReceive(value: number) {
    this.ammountReceived = value;
    return value;
  }

  onSubmit() {
    console.log(this.offerMaterial);  
    console.log(this.offerPlayer);  
    console.log(this.receiveMaterial);
    console.log(this.ammountGiven);
    console.log(this.ammountReceived);
  }
}
