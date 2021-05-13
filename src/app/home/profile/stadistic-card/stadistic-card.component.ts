import { Component, OnInit, Input} from '@angular/core';

@Component({
  selector: 'app-stadistic-card',
  templateUrl: './stadistic-card.component.html',
  styleUrls: ['./stadistic-card.component.sass']
})
export class StadisticCardComponent implements OnInit {

  @Input() Number: string;
  @Input() Text: string;

  constructor() { }

  ngOnInit(): void {
  }

}
