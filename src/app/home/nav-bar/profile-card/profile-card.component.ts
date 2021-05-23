import { Component, OnInit , Input} from '@angular/core';
import { Router } from '@angular/router';
import { LangService } from 'src/app/service/lang/lang.service';
import { UserService } from 'src/app/service/user/user.service';

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

  constructor(public userService: UserService, public langService: LangService, public router: Router) { }

  ngOnInit(): void {
  }

}
