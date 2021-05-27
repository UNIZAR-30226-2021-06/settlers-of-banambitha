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
import { ProfileCardComponent } from './nav-bar/profile-card/profile-card.component';
import { FriendBrowserComponent } from './social-bar/friend-browser/friend-browser.component';
import { FriendCardComponent } from './social-bar/friend-card/friend-card.component';
import {MatTabsModule} from '@angular/material/tabs';
import { ProfilePicturesComponent } from './shop/profile-pictures/profile-pictures.component';
import { BoardSkinsComponent } from './shop/board-skins/board-skins.component'; 
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatToolbarModule} from '@angular/material/toolbar'; 
import {MatListModule} from '@angular/material/list';
import {MatIconModule} from '@angular/material/icon';
import { TerrainComponent } from './rules/terrain/terrain.component';
import { ResourcesComponent } from './rules/resources/resources.component';
import { DevelopmentCardComponent } from './rules/development-card/development-card.component';
import { ActionsComponent } from './rules/actions/actions.component';
import { IngameComponent } from './rules/ingame/ingame.component';
import { StadisticCardComponent } from './profile/stadistic-card/stadistic-card.component';
import { MainHomeComponent } from './main-home/main-home.component';
import {MatProgressSpinnerModule} from '@angular/material/progress-spinner'; 
import { MatMenuModule} from '@angular/material/menu';
import { MatInputModule } from '@angular/material/input';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {MatExpansionModule} from '@angular/material/expansion'; 
import { MainProfileComponent } from './profile/main-profile/main-profile.component';
import { ChangeAvatarComponent } from './profile/change-avatar/change-avatar.component';
import { ChangePasswordComponent} from './profile/change-password/change-password.component'
import { ChangeLanguageComponent } from './profile/change-language/change-language.component';
import {MatSnackBarModule} from '@angular/material/snack-bar';
import { DeleteAccountComponent } from './profile/delete-account/delete-account.component';


@NgModule({
  declarations: [NavBarComponent, SocialBarComponent, PlayComponent,
    PlayerCardComponent, ShopComponent, SettingsComponent, ProfileComponent,
    RulesComponent, ProfilePicturesComponent, BoardSkinsComponent, TerrainComponent,
    ResourcesComponent, DevelopmentCardComponent, ActionsComponent, IngameComponent,
    ProfileCardComponent, FriendBrowserComponent, FriendCardComponent, MainHomeComponent,
    StadisticCardComponent,
    MainProfileComponent,
    ChangeAvatarComponent,
    ChangePasswordComponent,
    ChangeLanguageComponent,
    DeleteAccountComponent],
    
  imports: [
    CommonModule, 
    MatCardModule, 
    MatGridListModule, 
    MatChipsModule,
    MatButtonModule,
    MatSelectModule,
    MatDividerModule,
    HomeRoutingModule,
    FlexLayoutModule, 
    MatTabsModule,
    MatSidenavModule,
    MatToolbarModule,
    MatListModule,
    MatIconModule, 
    MatProgressSpinnerModule, 
    MatMenuModule,
    MatDividerModule, 
    MatInputModule,
    FormsModule, 
    MatExpansionModule,
    ReactiveFormsModule
  ],
  exports: [
    NavBarComponent, SocialBarComponent, PlayComponent, ShopComponent, ProfileComponent ,RulesComponent
  ]
})
export class HomeModule { }
