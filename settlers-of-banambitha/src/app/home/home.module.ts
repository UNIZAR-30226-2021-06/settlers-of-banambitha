import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlayComponent } from './play/play.component';
import {MatCardModule} from '@angular/material/card';
import { PlayerCardComponent } from './play/player-card/player-card.component';
import {MatGridListModule} from '@angular/material/grid-list'; 
import {MatChipsModule} from '@angular/material/chips';
import {MatButtonModule} from '@angular/material/button'; 
import {MatSelectModule} from '@angular/material/select'; 
import {MatDividerModule} from '@angular/material/divider'; 
import {FlexLayoutModule} from '@angular/flex-layout';
import { ShopComponent } from './shop/shop.component';
import { SettingsComponent } from './settings/settings.component';
import { ProfileComponent } from './profile/profile.component';
import { RulesComponent } from './rules/rules.component';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SocialBarComponent } from './social-bar/social-bar.component';
import { HomeRoutingModule } from './home-routing.module';


@NgModule({
  declarations: [NavBarComponent, SocialBarComponent, PlayComponent, PlayerCardComponent, ShopComponent, SettingsComponent, ProfileComponent, RulesComponent],
  imports: [
    CommonModule, 
    MatCardModule, 
    MatGridListModule, 
    MatChipsModule,
    MatButtonModule,
    MatSelectModule,
    MatDividerModule,
    FlexLayoutModule,
    HomeRoutingModule
  ],
  exports: [
    NavBarComponent, SocialBarComponent,PlayComponent, ShopComponent, ProfileComponent, RulesComponent
  ]
})
export class HomeModule { }
