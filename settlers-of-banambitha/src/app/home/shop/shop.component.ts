import { Component, OnInit } from '@angular/core';
import { LangService } from 'src/app/service/lang/lang.service';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.sass']
})
export class ShopComponent implements OnInit {

  constructor(public langService: LangService) { }

  ngOnInit(): void {
  }

}
