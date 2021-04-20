import { Component, OnInit } from '@angular/core';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.sass']
})
export class NavBarComponent implements OnInit {

  constructor(public UserService: UserService) { }

  public saldo;
  public userName;

  ngOnInit(): void {
    this.saldo = UserService.getSaldo()
    this.userName = UserService.getUsername()
  }

}
