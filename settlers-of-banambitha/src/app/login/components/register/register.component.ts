import { Component, Inject, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { MatDialog,MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass', '../../login.module.sass']
})
export class RegisterComponent implements OnInit {

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  hide = true;
  onSubmit(f: NgForm) {
    console.log(f.value);  // { first: '', last: '' }
    console.log(f.valid);  // false
  }

  openDialog() {
    this.dialog.open(DialogData, {
      data: {
        animal: 'panda'
      }
    });
  }

}

@Component({
  selector: 'dialog-data',
  templateUrl: 'dialog-data.html',
})
export class DialogData {
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {}
}