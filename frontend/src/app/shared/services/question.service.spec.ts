import { TestBed } from '@angular/core/testing';

import { QuestionService } from './question.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {AuthService} from "./auth.service";
import {provideHttpClient} from "@angular/common/http";
import {OrderSearchQuery} from "../model/order/orderSearchQuery";
import {QuestionSearchQuery} from "../model/question/questionSearchQuery";

describe('QuestionService', () => {
  let service: QuestionService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(QuestionService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set default page to 10 if not provided', () => {
    const mockResponse = { body: [] };

    service.listQuestions({} as QuestionSearchQuery).subscribe();

    const req = httpTestingController.expectOne((req) =>
        req.url.includes(service['apiUrl'])
    );
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('size')).toBe('10');

    req.flush(mockResponse);
  });

  it('should send provided query parameters', () => {
    const mockQuery: QuestionSearchQuery = { page: 2, size: 5};

    service.listQuestions(mockQuery).subscribe();

    const req = httpTestingController.expectOne((req) =>
        req.url.includes(service['apiUrl'])
    );
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('5');

    req.flush([]);
  });
});
