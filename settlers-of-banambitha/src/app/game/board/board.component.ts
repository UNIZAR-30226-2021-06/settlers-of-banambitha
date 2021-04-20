import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';
import { Router } from '@angular/router';


@Component({
  selector: 'app-board',
  templateUrl: './board.component.html',
  styleUrls: ['./board.component.scss']
})
export class BoardComponent implements OnInit {

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  openSettingsDialog() {
    this.dialog.open(SettingsDialog);

  }

}


@Component({
  selector: 'settings-dialog',
  templateUrl: 'settings-dialog.html',
  styleUrls: ['settings-dialog.sass']
})
export class SettingsDialog {
  constructor(@Inject(MAT_DIALOG_DATA) public data: SettingsDialog, private router:Router) {
    
  }

  goHome() {
    //<a routerLink="/home/play" routerLinkActive="active">Jugar</a>

    this.router.navigate(['/home/play'])

  }

}