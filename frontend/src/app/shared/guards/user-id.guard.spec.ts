import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { userIdGuard } from './user-id.guard';

describe('userIdGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => userIdGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
