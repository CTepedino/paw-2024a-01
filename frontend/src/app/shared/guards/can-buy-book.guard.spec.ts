import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canBuyBookGuard } from './can-buy-book.guard';

describe('canBuyBookGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canBuyBookGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
