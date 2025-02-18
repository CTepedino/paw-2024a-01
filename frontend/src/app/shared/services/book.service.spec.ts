import { TestBed } from '@angular/core/testing';

import { BookService } from './book.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {BookSearchQuery} from "../model/book/bookSearchQuery";
import {ReviewSearchQuery} from "../model/review/reviewSearchQuery";

describe('BookService', () => {
  let service: BookService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BookService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(BookService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should list books with default size of 10 if size is not provided', () => {
    const mockResponse = {body: []};

    service.listBooks({}).subscribe();

    const req = httpTestingController.expectOne((req) => req.url.includes(service["apiURL"]));
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('size')).toBe('10');

    req.flush(mockResponse);
  });

  it('should list books with provided query parameters', () => {
    const mockQuery: BookSearchQuery = { size: 5, title: 'Angular' };

    service.listBooks(mockQuery).subscribe();

    const req = httpTestingController.expectOne((req) => req.url.includes(service["apiURL"]));
    expect(req.request.params.get('size')).toBe('5');
    expect(req.request.params.get('title')).toBe('Angular');

  });

  it('should list reviews with default size of 10 if size is not provided', () => {
    const mockResponse = {body: []};

    service.listReviews('bookUrl',{}).subscribe();

    const req = httpTestingController.expectOne((req) => req.url.includes('bookUrl/reviews'));
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('size')).toBe('10');

    req.flush(mockResponse);
  });

  it('should list reviews with provided query parameters', () => {
    const mockQuery: ReviewSearchQuery = { size: 5, page: 2 };

    service.listReviews('bookUrl', mockQuery).subscribe();

    const req = httpTestingController.expectOne((req) => req.url.includes('bookUrl/reviews'));
    expect(req.request.params.get('size')).toBe('5');
    expect(req.request.params.get('page')).toBe('2');

  });
});
