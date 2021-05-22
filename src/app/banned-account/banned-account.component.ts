import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LangService } from '../service/lang/lang.service';
import { UserService } from '../service/user/user.service';

@Component({
  selector: 'app-banned-account',
  templateUrl: './banned-account.component.html',
  styleUrls: ['./banned-account.component.sass']
})
export class BannedAccountComponent implements OnInit {

  constructor(public router: Router, public userService: UserService, public langService: LangService) { }

  ngOnInit(): void {
  }

}
