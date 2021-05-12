import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'rules-actions',
  templateUrl: './actions.component.html',
  styleUrls: ['../rules.component.sass']
})
export class ActionsComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

}
