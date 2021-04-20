import { Component, NgModule, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home-play',
  templateUrl: './play.component.html',
  styleUrls: ['./play.component.sass'],
})


export class PlayComponent implements OnInit {

  constructor(private router:Router) { }

  ngOnInit(): void {
  }

  goGame() {
    this.router.navigate(['/board'])
  }
}
