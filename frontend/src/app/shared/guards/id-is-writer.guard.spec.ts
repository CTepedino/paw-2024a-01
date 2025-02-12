import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { idIsWriterGuard } from './id-is-writer.guard';

describe('idIsWriterGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => idIsWriterGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
