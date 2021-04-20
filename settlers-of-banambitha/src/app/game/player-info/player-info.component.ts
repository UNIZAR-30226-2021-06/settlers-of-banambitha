import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';


@Component({
  selector: 'app-player-info',
  templateUrl: './player-info.component.html',
  styleUrls: ['./player-info.component.sass']
})
export class PlayerInfoComponent implements OnInit {

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }
  openCardsDialog() {
    this.dialog.open(DialogData);
  }

  openInternalTradeDialog() {
    this.dialog.open(InternalTradeDialog);
  }

  openExternalTradeDialog() {
    this.dialog.open(ExternalTradeDialog);
  }
}



@Component({
  selector: 'cards-dialog',
  templateUrl: 'cards-dialog.html',
  styleUrls: ['./player-info.component.sass']
})


export class DialogData {
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {}

  cartasCaballero=2
  cartasCarretera=5
  cartasDescubrimiento=1
  puntosVictoria=8
}



interface Player {
  value: string;
  viewValue: string;
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
  offerMaterial: string;
  offerPlayer: string;
  receiveMaterial: string;
  ammountGiven:number;
  ammountReceived:number;

  constructor(public dialogRef: MatDialogRef<InternalTradeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.formatLabelOffer = this.formatLabelOffer.bind(this);
    this.formatLabelReceive = this.formatLabelReceive.bind(this);
   }

   ngOnInit(): void {
  }

   materials: Material[] = [
    { value: 'Madera', viewValue: 'Madera'},
    { value: 'Lana', viewValue: 'Lana'},
    { value: 'Cereales', viewValue: 'Cereales'},
    { value: 'Arcilla', viewValue: 'Arcilla'},
    { value: 'Mineral', viewValue: 'Mineral'}
  ];


  players: Player[] = [
    {value: 'player-2', viewValue: 'Player2'},
    {value: 'player-3', viewValue: 'Player3'},
    {value: 'player-4', viewValue: 'Player4'}
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


interface Ratio {
  value: string;
  viewValue: string;
}


@Component({
  selector: 'app-trade-external',
  templateUrl: 'external-trade-dialog.html',
  styleUrls: ['./trade.dialog.sass']
})
export class ExternalTradeDialog {
  offerMaterial: string;
  receiveMaterial: string;
  ratioSelected: string;


  constructor(public dialogRef: MatDialogRef<InternalTradeDialog>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData) {}


   materials: Material[] = [
    { value: 'Madera', viewValue: 'Madera'},
    { value: 'Lana', viewValue: 'Lana'},
    { value: 'Cereales', viewValue: 'Cereales'},
    { value: 'Arcilla', viewValue: 'Arcilla'},
    { value: 'Mineral', viewValue: 'Mineral'}
  ];

  ratios: Ratio[] = [
    { value: '4-1', viewValue: '4-1'},
    { value: '3-1', viewValue: '3-1'},
    { value: '2-1', viewValue: '2-1'}
  ];

  onSubmit() {
    console.log(this.offerMaterial);  
    console.log(this.ratioSelected);
    console.log(this.receiveMaterial);
  }


}