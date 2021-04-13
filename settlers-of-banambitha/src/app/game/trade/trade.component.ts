import { Component, OnInit } from '@angular/core';


interface Player {
  value: string;
  viewValue: string;
}

interface Material {
  value: string;
  viewValue: string;
}


@Component({
  selector: 'app-trade',
  templateUrl: './trade.component.html',
  styleUrls: ['./trade.component.sass']
})
export class TradeComponent implements OnInit {
  offerMaterial: string;
  constructor() { }

  ngOnInit(): void {
  }



  players: Player[] = [
    {value: 'player-2', viewValue: 'Player2'},
    {value: 'player-3', viewValue: 'Player3'},
    {value: 'player-4', viewValue: 'Player4'}
  ];

  formatLabel(value: number) {
    if (value >= 1000) {
      return Math.round(value / 1000) + 'k';
    }

    return value;
  }
}
