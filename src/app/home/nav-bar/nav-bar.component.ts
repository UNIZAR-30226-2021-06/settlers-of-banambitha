import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.sass']
})
export class NavBarComponent implements OnInit {

  constructor(public userService: UserService, public langService : LangService) { }


  ngOnInit(): void {
  }

}
