import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HexNumberComponent } from './hex-number.component';

describe('HexNumberComponent', () => {
  let component: HexNumberComponent;
  let fixture: ComponentFixture<HexNumberComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HexNumberComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(HexNumberComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
