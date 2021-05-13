import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'rules-ingame',
  templateUrl: './ingame.component.html',
  styleUrls: ['../rules.component.sass']
})
export class IngameComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

}
