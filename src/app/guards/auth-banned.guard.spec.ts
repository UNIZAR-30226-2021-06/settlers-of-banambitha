import { TestBed } from '@angular/core/testing';

import { AuthBannedGuard } from './auth-banned.guard';

describe('AuthBannedGuard', () => {
  let guard: AuthBannedGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(AuthBannedGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
