import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'rules-terrain',
  templateUrl: './terrain.component.html',
  styleUrls: ['../rules.component.sass']
})
export class TerrainComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

}
