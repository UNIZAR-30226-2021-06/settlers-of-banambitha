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





@NgModule({
  declarations: [GameplayComponent, BoardComponent, ChatComponent, StatsComponent, PlayerInfoComponent],
  imports: [
    CommonModule,
    BrowserModule,
    BrowserAnimationsModule,
    MatCardModule,
    FlexLayoutModule,
    MatIconModule,
    MatTableModule,
    MatListModule
  ],
  exports: [GameplayComponent]
})
export class GameModule { }
