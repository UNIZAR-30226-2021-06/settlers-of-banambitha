import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StadisticCardComponent } from './stadistic-card.component';

describe('StadisticCardComponent', () => {
  let component: StadisticCardComponent;
  let fixture: ComponentFixture<StadisticCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StadisticCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(StadisticCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
