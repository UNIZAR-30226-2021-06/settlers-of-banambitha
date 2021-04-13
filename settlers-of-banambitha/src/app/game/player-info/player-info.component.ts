import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA } from '@angular/material/dialog';
import {TradeComponent} from '../trade/trade.component';

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

  openTradeDialog() {
    this.dialog.open(TradeComponent);
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


