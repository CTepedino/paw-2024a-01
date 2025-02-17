import { TestBed } from '@angular/core/testing';

import { EditBookService } from './edit-book.service';

describe('EditBookService', () => {
  let service: EditBookService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EditBookService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
