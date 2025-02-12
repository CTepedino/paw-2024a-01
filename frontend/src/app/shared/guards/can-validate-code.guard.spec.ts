import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { canValidateCodeGuard } from './can-validate-code.guard';

describe('canValidateCodeGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canValidateCodeGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
