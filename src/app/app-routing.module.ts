import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { GameplayComponent } from './game/gameplay/gameplay.component';
import { AuthBoardGuard } from './guards/auth-board.guard';
import { AuthHomeGuard } from './guards/auth-home.guard';
import { AuthLoginGuard } from './guards/auth-login.guard';
import { HomeModule } from './home/home.module';
import { MainHomeComponent } from './home/main-home/main-home.component';
import { PlayComponent } from './home/play/play.component';
import { ProfileComponent } from './home/profile/profile.component';
import { RulesComponent } from './home/rules/rules.component';
import { ShopComponent } from './home/shop/shop.component';
import { MainLoginComponent } from './login/components/main-login/main-login.component';
import { RegisterComponent } from './login/components/register/register.component';
import { ResetPasswordComponent } from './login/components/reset-password/reset-password.component';
import { SigninComponent } from './login/components/signin/signin.component';
import { ChangeAvatarComponent } from './home/profile/change-avatar/change-avatar.component';
import { ChangePasswordComponent } from './home/profile/change-password/change-password.component';
import { MainProfileComponent } from './home/profile/main-profile/main-profile.component';
import { ChangeLanguageComponent} from './home/profile/change-language/change-language.component';

const routes: Routes = [ 

  {path:'',redirectTo:'login/signin', pathMatch: 'full' },
  {path:'login',redirectTo:'login/signin', pathMatch: 'full' },
  {path:'home',redirectTo:'home/play', pathMatch: 'full' },

  {path: 'login', component: MainLoginComponent, 
   canActivate: [AuthLoginGuard],
   children: [
    {path: 'signin', component: SigninComponent },
    {path: 'register', component: RegisterComponent},
    {path: 'reset-password', component: ResetPasswordComponent}
   ]
  },

  {path: 'home', component: MainHomeComponent,
   canActivate: [AuthHomeGuard],
   children: [
    {path: 'play', component: PlayComponent },
    {path: 'profile', component: MainProfileComponent,
    children: [
      {path: 'change-avatar', component: ChangeAvatarComponent},
      {path: 'change-password', component: ChangePasswordComponent},
      {path: 'change-language', component: ChangeLanguageComponent},
      {path: '', component: ProfileComponent}
    ]},
    {path: 'rules', component: RulesComponent},
    {path: 'shop', component: ShopComponent},
    ]
   },

  {path: 'board', component: GameplayComponent, 
    canActivate: [AuthBoardGuard]}

];

@NgModule({
  imports: [RouterModule.forRoot(routes), HomeModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }


