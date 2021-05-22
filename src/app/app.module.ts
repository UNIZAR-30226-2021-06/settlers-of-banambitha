import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeModule } from './home/home.module';
import { LoginModule } from './login/login.module';
import { ServiceModule } from './service/service.module';
import { GameModule } from './game/game.module';
import { BannedAccountComponent } from './banned-account/banned-account.component';
import { MatButtonModule } from '@angular/material/button'; 



@NgModule({
  declarations: [
    AppComponent,
    BannedAccountComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule, 
    HomeModule,
    LoginModule,
    ServiceModule,
    GameModule, 
    MatButtonModule
],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { 
}
