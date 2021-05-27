import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { NgForm } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { LangService, Language } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';
import { ValidatorService } from '../validator.service';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.sass']
})
export class ChangePasswordComponent implements OnInit {

  phOldPassword: String;
  phNewPassword: String;
  phCNewPassword: String;

  updatePasswordForm: FormGroup;
  oldPassword: FormControl;
  newPassword: FormControl;
  cnewPassword: FormControl;
  constructor(private snackBar: MatSnackBar, private userService: UserService,  public langService: LangService, private validatorService: ValidatorService) { }

  ngOnInit(): void {
    this.phOldPassword = this.langService.get("old-password-f")
    this.phNewPassword = this.langService.get("new-password-f")
    this.phCNewPassword = this.langService.get("c-new-password-f")
    this.oldPassword = new FormControl('', [Validators.required])
    this.newPassword = new FormControl('', [Validators.required, Validators.pattern("^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?).{8,}$")] )
    this.cnewPassword = new FormControl('', [Validators.required, this.validatorService.MustMatch(this.newPassword)])

    this.updatePasswordForm = new FormGroup({
      'oldPassword' : this.oldPassword,
      'newPassword' : this.newPassword,
      'cnewPassword' : this.cnewPassword,
    });


  }

  onSubmit() {
    if(this.updatePasswordForm.valid) {
      this.userService.CambiarContraseña(this.oldPassword.value, this.newPassword.value);
      this.updatePasswordForm.reset();
    }
  }

}
