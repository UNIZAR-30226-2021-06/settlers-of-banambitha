import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'rules-resources',
  templateUrl: './resources.component.html',
  styleUrls: ['../rules.component.sass']
})
export class ResourcesComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

}
