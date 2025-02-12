import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { isNotBookWriterGuard } from './is-not-book-writer.guard';

describe('isNotBookWriterGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => isNotBookWriterGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
