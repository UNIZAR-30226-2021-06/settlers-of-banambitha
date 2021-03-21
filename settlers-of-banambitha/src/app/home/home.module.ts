import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NavBarComponent } from './nav-bar/nav-bar.component';
import { SocialBarComponent } from './social-bar/social-bar.component';
import { FirstComponent } from './first/first.component';
import { SecongComponent } from './secong/secong.component';

@NgModule({
  declarations: [NavBarComponent, SocialBarComponent, FirstComponent, SecongComponent],
  imports: [CommonModule],
  exports: [NavBarComponent, SocialBarComponent,  FirstComponent, SecongComponent]
})
export class HomeModule { }
