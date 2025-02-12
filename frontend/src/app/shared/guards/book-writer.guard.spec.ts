import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { bookWriterGuard } from './book-writer.guard';

describe('bookWriterGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => bookWriterGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
