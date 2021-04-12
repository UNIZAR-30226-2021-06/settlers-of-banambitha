import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FriendBrowserComponent } from './friend-browser.component';

describe('FriendBrowserComponent', () => {
  let component: FriendBrowserComponent;
  let fixture: ComponentFixture<FriendBrowserComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FriendBrowserComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FriendBrowserComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
