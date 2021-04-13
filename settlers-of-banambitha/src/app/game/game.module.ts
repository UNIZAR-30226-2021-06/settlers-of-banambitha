import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { GameplayComponent } from './gameplay/gameplay.component';
import { MatCardModule } from '@angular/material/card';
import { FlexLayoutModule } from '@angular/flex-layout';
import {MatIconModule} from '@angular/material/icon';
import { MatTableModule } from '@angular/material/table';
import {MatListModule} from '@angular/material/list';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { BrowserModule } from '@angular/platform-browser';
import { BoardComponent } from './board/board.component';
import { ChatComponent } from './chat/chat.component';
import { StatsComponent } from './stats/stats.component';
import { PlayerInfoComponent } from './player-info/player-info.component';
import { TradeComponent } from './trade/trade.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import {MatSliderModule} from '@angular/material/slider';
import { FormsModule } from '@angular/forms';





@NgModule({
  declarations: [
    GameplayComponent, 
    BoardComponent, 
    ChatComponent, 
    StatsComponent, 
    PlayerInfoComponent,
    TradeComponent],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatCardModule,
    FlexLayoutModule,
    MatIconModule,
    MatTableModule,
    MatListModule,
    MatFormFieldModule,
    MatSelectModule,
    MatSliderModule,
    FormsModule
  ],
  exports: [GameplayComponent]
})
export class GameModule { }
