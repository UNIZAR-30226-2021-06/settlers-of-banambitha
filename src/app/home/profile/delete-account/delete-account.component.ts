import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { GameService } from 'src/app/service/game/game.service';
import { LangService } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';
import { ValidatorService } from '../validator.service';

@Component({
  selector: 'app-delete-account',
  templateUrl: './delete-account.component.html',
  styleUrls: ['./delete-account.component.sass']
})
export class DeleteAccountComponent implements OnInit {

  phOldPassword: String;
  phNewPassword: String;
  phCNewPassword: String;

  updatePasswordForm: FormGroup;
  oldPassword: FormControl;
  constructor(private snackBar: MatSnackBar, private userService: UserService,  public langService: LangService,
              private validatorService: ValidatorService, public gameService: GameService) {

  }

  ngOnInit(): void {
    this.oldPassword = new FormControl('', [Validators.required])

    this.updatePasswordForm = new FormGroup({
      'oldPassword' : this.oldPassword,
    });


  }

  onSubmit() {
    if(this.updatePasswordForm.valid) {
      this.gameService.eliminarCuenta(this.oldPassword.value)
      this.updatePasswordForm.reset();
    }
  }

}
