import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-signin',
  templateUrl: './signin.component.html',
  styleUrls: ['./signin.component.sass', '../../login.module.sass']
})
export class SigninComponent implements OnInit {

  constructor() { }

  ngOnInit(): void {
  }

  hide = true;
}
