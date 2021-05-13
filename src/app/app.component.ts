import { Component } from '@angular/core';
import { NavigationStart, RouterOutlet } from '@angular/router';
import { Router } from '@angular/router';
import { UserService  } from './service/user/user.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.sass']
})
export class AppComponent {
  title = 'settlers-of-banambitha';

  constructor(private router: Router, public UserService: UserService){}

  ngOnInit(){
    /* 
    let that = this
    this.router.events.subscribe(event =>{
      if (event instanceof NavigationStart) {
        console.log("Checkear aquí sesión")
      }
    })

    console.log("Obtén cosas");
    (async () => {
      try {
        await this.UserService.findUser("anye")
      } catch (e){
        console.log(e) 
        console.log("mi error pillado")
      }
    })()
    */

  }


}
