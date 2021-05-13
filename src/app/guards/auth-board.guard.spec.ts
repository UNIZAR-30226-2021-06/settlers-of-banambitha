import { TestBed } from '@angular/core/testing';

import { AuthBoardGuard } from './auth-board.guard';

describe('AuthBoardGuard', () => {
  let guard: AuthBoardGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(AuthBoardGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
