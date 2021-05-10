import { Component, OnInit } from '@angular/core';
import { ValidationErrors } from '@angular/forms';
import { AsyncValidatorFn } from '@angular/forms';
import { FormGroup } from '@angular/forms';
import { Validators } from '@angular/forms';
import { FormControl } from '@angular/forms';
import { AbstractControl } from '@angular/forms';
import { NgForm } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { LangService, Language } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';

class usernameValidator {

  static createValidator(UserService: UserService): AsyncValidatorFn{
    return (control: AbstractControl): Observable<ValidationErrors> => {
      return UserService.findUserObservable(control.value).pipe(
        map((result) => result.hasOwnProperty("nombre") ? null : {invalidAsync: true}), catchError( err => {return of({invalidAsync: true})}  )
      )
    }
  }
}

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.sass', '../../login.module.sass']
})
export class SigninComponent implements OnInit {
  
  public showError: Boolean = false

  public loading: Boolean = false
  usernameForm: FormGroup
  usernameSyncValidators = [
    Validators.required,
    Validators.pattern("^(?=.*[0-9]*)(?=.*[a-z]*)(?=.*[A-Z]*)(?=.*[-_]*).{5,32}$")
  ]

  constructor(private userService: UserService, private router: Router, public langService: LangService) {

       }

  ngOnInit(): void {

    this.usernameForm = new FormGroup({
      username: new FormControl('',
        this.usernameSyncValidators,
        usernameValidator.createValidator(this.userService)
      )
    })
  }

  hide = true;
  
  onSubmit(f: NgForm) {
    (async () => {
      try{
        this.loading = true
        await this.userService.validate(this.usernameForm.value["username"], f.value["password"])
        this.router.navigate(['/home']) 
        this.showError = false
      }catch (e){
        this.showError = true
      } finally{
        this.loading = false
      }
    })()
  }

  public cl(): void {
    this.langService.selectedLang = Language.ENG
  }

}
