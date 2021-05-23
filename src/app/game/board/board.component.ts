import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { GameService, TipoAsentamiento, TipoTerreno } from 'src/app/service/game/game.service';
import { LangService } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  public static readonly player1Color: String = "#056396"
  public static readonly player2Color: String = "#96054b"
  public static readonly player3Color: String = "#d2eb34"
  public static readonly player4Color: String = "#49a607"

  public static readonly imageBaseUrl = "../../../assets/images/board"

  public readonly hexMapping: Map<TipoTerreno, String> = new Map<TipoTerreno,String>([
   [TipoTerreno.BOSQUE,   "wood"], 
   [TipoTerreno.CERRO ,   "brick"], 
   [TipoTerreno.DESIERTO, "sand"], 
   [TipoTerreno.MONTANYA, "coal"], 
   [TipoTerreno.PASTO,    "sheep"], 
   [TipoTerreno.SEMBRADO, "wheat"], 
  ]
  );

  constructor(public dialog: MatDialog, public gameService: GameService, public langService: LangService, public userService: UserService) { }

  ngOnInit(): void {
  }

  openSettingsDialog() {
    this.dialog.open(SettingsDialog);

  }

  public tipoTerreno(): typeof TipoTerreno {
    return TipoTerreno
  }

  public getSkinUrl(tp: TipoTerreno): String{
    console.log( BoardComponent.imageBaseUrl + "/" + this.userService.apariencia  + "/" + tp + ".jpg")
    return BoardComponent.imageBaseUrl + "/" + this.userService.apariencia  + "/" + tp + ".jpg"
  }

  public getWaterUrl(): String {
    return BoardComponent.imageBaseUrl + "/" + this.userService.apariencia + "/" + "Agua.jpg"
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