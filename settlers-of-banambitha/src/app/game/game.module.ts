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
import { PlayerInfoComponent, InternalTradeDialog, ExternalTradeDialog } from './player-info/player-info.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import {MatSliderModule} from '@angular/material/slider';
import { FormsModule } from '@angular/forms';
import { MatDialogModule } from '@angular/material/dialog';
import { AppComponent } from '../app.component';
import {MatMenuModule} from '@angular/material/menu';
import {MatButtonModule} from '@angular/material/button';
import { SettlementComponent } from './board/settlement/settlement.component';
import { RoadComponent } from './board/road/road.component'; 
import {MatGridListModule} from '@angular/material/grid-list'
import {MatExpansionModule} from '@angular/material/expansion';
import {MatTabsModule} from '@angular/material/tabs';
import {MatProgressBarModule} from '@angular/material/progress-bar';



@NgModule({
  declarations: [
    GameplayComponent, 
    BoardComponent, 
    ChatComponent, 
    StatsComponent, 
    PlayerInfoComponent,
    InternalTradeDialog,
    ExternalTradeDialog,
    SettlementComponent,
    RoadComponent],
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
    FormsModule,
    MatDialogModule,
    MatMenuModule, 
    MatGridListModule,
    MatExpansionModule, 
    MatTabsModule,
    MatButtonModule,
    MatProgressBarModule
  ],
  exports: [GameplayComponent],
  entryComponents: [InternalTradeDialog,ExternalTradeDialog],
  bootstrap: [AppComponent]
})
export class GameModule { }
