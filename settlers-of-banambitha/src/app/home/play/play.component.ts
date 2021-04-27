import { Component, NgModule, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from 'src/app/service/user/user.service';
import { WsService } from 'src/app/service/ws/ws.service';

@Component({
  selector: 'app-home-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.sass'],
})


export class PlayComponent implements OnInit {

  constructor(private router:Router, private UserService: UserService, private WsService: WsService) { }

  ngOnInit(): void {
  }

  goGame() {
  }

  goOnline() {

  }

  goPrivada() {
  }
}
