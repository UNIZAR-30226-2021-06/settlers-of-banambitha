import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BoardSkinsComponent } from './board-skins.component';

describe('BoardSkinsComponent', () => {
  let component: BoardSkinsComponent;
  let fixture: ComponentFixture<BoardSkinsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BoardSkinsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BoardSkinsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
