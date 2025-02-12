import { TestBed } from '@angular/core/testing';

import { BookWithDataService } from './book-with-data.service';

describe('BookWithDataService', () => {
  let service: BookWithDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BookWithDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
