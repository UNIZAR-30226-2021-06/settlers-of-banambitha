import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GameService, TipoTerreno } from 'src/app/service/game/game.service';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {


  terrenoDesierto = TipoTerreno.DESIERTO


  public hexMapping: Map<TipoTerreno, String> = new Map<TipoTerreno,String>([
   [TipoTerreno.BOSQUE,   "wood"], 
   [TipoTerreno.CERRO ,   "coal"], 
   [TipoTerreno.DESIERTO, "sand"], 
   [TipoTerreno.MONTANYA, "brick"], 
   [TipoTerreno.PASTO,    "sheep"], 
   [TipoTerreno.SEMBRADO, "wheat"], 
  ]
  )

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