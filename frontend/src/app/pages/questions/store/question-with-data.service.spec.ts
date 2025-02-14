import { TestBed } from '@angular/core/testing';

import { QuestionWithDataService } from './question-with-data.service';

describe('QuestionWithDataService', () => {
  let service: QuestionWithDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionWithDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
