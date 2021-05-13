import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.sass', '../../login.module.sass']
})
export class ResetPasswordComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

  onSubmit(f: NgForm) {
    console.log(f.value);  // { first: '', last: '' }
    console.log(f.valid);  // false
  }
}
