import { Component, Inject, OnInit } from '@angular/core';
import { AbstractControl, AsyncValidatorFn, FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import { Validators } from '@angular/forms';
import { NgForm } from '@angular/forms';
import { MatDialog,MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { UserService } from 'src/app/service/user/user.service';


class usernameValidator {

  static createValidator(UserService: UserService): AsyncValidatorFn{
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return UserService.findUserObservable(control.value).pipe(
        map((result) => result.hasOwnProperty("nombre") ? {invalidAsync: true} : null), catchError( err => {return of([])}  )
      )
    }
  }
}

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.sass', '../../login.module.sass']
})
export class RegisterComponent implements OnInit {

  public errorMsg: String 
  usernameForm: FormGroup
  usernameSyncValidators = [
    Validators.required,
    Validators.pattern("^(?=.*[0-9]*)(?=.*[a-z]*)(?=.*[A-Z]*)(?=.*[-_]*).{5,32}$")
  ]

  constructor(public dialog: MatDialog, private UserService: UserService) { }

  ngOnInit(): void {
    this.usernameForm = new FormGroup({
      username: new FormControl('',
        this.usernameSyncValidators,
        usernameValidator.createValidator(this.UserService)
      )
    })
  }

  hide = true;
  onSubmit(f: NgForm) {
    console.log(f.value);  
    console.log(this.usernameForm.value["username"])
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