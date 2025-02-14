import { TestBed } from '@angular/core/testing';

import { QuestionWithDataServiceService } from './question-with-data-service.service';

describe('QuestionWithDataServiceService', () => {
  let service: QuestionWithDataServiceService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(QuestionWithDataServiceService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
