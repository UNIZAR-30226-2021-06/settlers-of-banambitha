import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BannedAccountComponent } from './banned-account.component';

describe('BannedAccountComponent', () => {
  let component: BannedAccountComponent;
  let fixture: ComponentFixture<BannedAccountComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BannedAccountComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(BannedAccountComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
