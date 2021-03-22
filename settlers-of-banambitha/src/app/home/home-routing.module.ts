import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Routes, RouterModule } from '@angular/router';
import { PlayComponent } from './play/play.component';
import { ProfileComponent } from './profile/profile.component';
import { RulesComponent } from './rules/rules.component';
import { ShopComponent } from './shop/shop.component';

const routes: Routes = [
  {path: 'play', component: PlayComponent },
  {path: 'profile', component: ProfileComponent},
  {path: 'rules', component: RulesComponent},
  {path: 'shop', component: ShopComponent}
];

@NgModule({
  declarations: [],
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
  
})
export class HomeRoutingModule { }