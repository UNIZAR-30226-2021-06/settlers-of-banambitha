import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'rules-development-card',
  templateUrl: './development-card.component.html',
  styleUrls: ['../rules.component.sass']
})
export class DevelopmentCardComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

}
