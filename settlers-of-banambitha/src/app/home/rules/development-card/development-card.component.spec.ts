import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DevelopmentCardComponent } from './development-card.component';

describe('DevelopmentCardComponent', () => {
  let component: DevelopmentCardComponent;
  let fixture: ComponentFixture<DevelopmentCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DevelopmentCardComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(DevelopmentCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
