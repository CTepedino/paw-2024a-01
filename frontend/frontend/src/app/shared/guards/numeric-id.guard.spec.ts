import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { numericIDGuard } from './numeric-id.guard';

describe('numericIDGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => numericIDGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
