import { Component, OnInit , Input} from '@angular/core';

@Component({
  selector: 'app-friend-card',
  templateUrl: './friend-card.component.html',
  styleUrls: ['./friend-card.component.sass']
})
export class FriendCardComponent implements OnInit {
  @Input() name: string;
  @Input() elo: string;
  @Input() profilePic: string;

  constructor() { }

  ngOnInit(): void {
  }

}
