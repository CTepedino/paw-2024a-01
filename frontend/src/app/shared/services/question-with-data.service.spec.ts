import { TestBed } from '@angular/core/testing';

import { QuestionWithDataService } from './question-with-data.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('QuestionWithDataService', () => {
  let service: QuestionWithDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });
    service = TestBed.inject(QuestionWithDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
