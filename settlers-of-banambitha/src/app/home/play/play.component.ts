import { Component, NgModule, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GameService } from 'src/app/service/game/game.service';
import { RoomService } from 'src/app/service/room/room.service';
import { UserService } from 'src/app/service/user/user.service';

@Component({
  selector: 'app-home-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.sass'],
})


export class PlayComponent implements OnInit {

  constructor(private router:Router, public UserService: UserService, public roomService: RoomService, public gameService: GameService) { }

  ngOnInit(): void {
  }

}
