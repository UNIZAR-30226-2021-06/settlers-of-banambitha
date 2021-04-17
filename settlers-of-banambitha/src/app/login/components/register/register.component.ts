import { Component, Inject, OnInit } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Validators } from '@angular/forms';
import { NgForm } from '@angular/forms';
import { MatDialog,MAT_DIALOG_DATA } from '@angular/material/dialog';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass', '../../login.module.sass']
})
export class RegisterComponent implements OnInit {

  public errorMsg: String 

  constructor(public dialog: MatDialog) { }

  ngOnInit(): void {
  }

  hide = true;
  onSubmit(f: NgForm) {
    console.log(f.value);  
    console.log(f.valid);  
  }

  openDialog() {
    this.dialog.open(DialogData);
  }

}

@Component({
  selector: 'dialog-data',
  templateUrl: 'dialog-data.html',
})
export class DialogData {
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {}
}