import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeModule } from './home/home.module';
import { LoginModule } from './login/login.module';
import { ServiceModule } from './service/service.module';
import { GameModule } from './game/game.module';




@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule, 
    HomeModule,
    LoginModule,
    ServiceModule,
    GameModule
],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { 
}
