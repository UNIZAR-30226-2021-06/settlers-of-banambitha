import { Component, OnInit , Input} from '@angular/core';

@Component({
  selector: 'app-profile-card',
  templateUrl: './profile-card.component.html',
  styleUrls: ['./profile-card.component.sass']
})
export class ProfileCardComponent implements OnInit {

  @Input() playerELO: bigint
  @Input() playerName: bigint
  @Input() playerCoins: bigint
  @Input() playerAvatar: string

  constructor() { }

  ngOnInit(): void {
  }

}
