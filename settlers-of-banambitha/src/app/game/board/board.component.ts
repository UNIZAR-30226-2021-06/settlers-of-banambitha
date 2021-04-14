import { Component, Inject, OnInit } from '@angular/core';
import { MatDialog,MAT_DIALOG_DATA,MatDialogRef } from '@angular/material/dialog';


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
  constructor(@Inject(MAT_DIALOG_DATA) public data: SettingsDialog) {}

}