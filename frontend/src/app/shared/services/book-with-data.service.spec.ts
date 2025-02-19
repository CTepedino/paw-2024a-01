import { TestBed } from '@angular/core/testing';

import { BookWithDataService } from './book-with-data.service';
import jasmine from "jasmine";
import {BookService} from "./book.service";
import {UserService} from "./user.service";
import {Book} from "../model/book/book";
import {User} from "../model/user/user";
import {Deal} from "../model/book/deal";
import {PaginatedContent} from "../model/paginatedContent";
import {of} from "rxjs";
import {HttpClient, provideHttpClient} from "@angular/common/http";
import {HttpClientTestingModule, provideHttpClientTesting} from "@angular/common/http/testing";

describe('BookWithDataService', () => {

  let service: BookWithDataService;
  let bookService: BookService;
  let userService: UserService;

  const mockBook: Book = { id: 1, title: 'Test Book', writer: 'writer-url', deal: 'deal-url' };
  const mockUser: User = { id: 1, firstName: 'Don',  lastName: 'Nadie' };
  const mockDeal: Deal = { id: 1, price: 100 };
  const mockPaginatedBooks: PaginatedContent<Book> = {
    data: [mockBook],
    pagination: { totalCount: 1, pageCount: 1 }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BookWithDataService,
        BookService,
        UserService,
        provideHttpClient(),
        provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(BookWithDataService);
    bookService = TestBed.inject(BookService);
    userService = TestBed.inject(UserService);

  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should return books with writer and deal info', (done) => {
    spyOn(bookService, 'listBooks').and.returnValue(of(mockPaginatedBooks));
    spyOn(bookService, 'getBookById').and.returnValue(of(mockBook));
    spyOn(bookService, 'getDeal').and.returnValue(of(mockDeal));
    spyOn(userService, 'getUser').and.returnValue(of(mockUser));

    service.listBooksWithData().subscribe(result => {
      expect(result.data.length).toBe(1);
      expect(result.data[0].writerInfo).toEqual(mockUser);
      expect(result.data[0].dealInfo).toEqual(mockDeal);
      expect(bookService.listBooks).toHaveBeenCalled();
      expect(userService.getUser).toHaveBeenCalledWith('writer-url');
      expect(bookService.getDeal).toHaveBeenCalledWith('deal-url');
      done();
    });
  });


  it('should return a single book with complete data', (done) => {
    spyOn(bookService, 'listBooks').and.returnValue(of(mockPaginatedBooks));
    spyOn(bookService, 'getBookById').and.returnValue(of(mockBook));
    spyOn(bookService, 'getDeal').and.returnValue(of(mockDeal));
    spyOn(userService, 'getUser').and.returnValue(of(mockUser));


    service.getBookWithData(1).subscribe(result => {
      expect(result.writerInfo).toEqual(mockUser);
      expect(result.dealInfo).toEqual(mockDeal);
      expect(bookService.getBookById).toHaveBeenCalledWith(1);
      done();
    });
  });

  it('should return a single book with writer info, but no deal info', (done) => {
    const deallessMockBook: Book = {id: 2, title: 'This book does not have a deal', writer: 'greedy-writer-url'};
    const mockPaginatedBooksNoDeal: PaginatedContent<Book> = {
      data: [deallessMockBook],
      pagination: { totalCount: 1, pageCount: 1 }
    };

    spyOn(bookService, 'listBooks').and.returnValue(of(mockPaginatedBooksNoDeal));
    spyOn(bookService, 'getBookById').and.returnValue(of(deallessMockBook));
    spyOn(userService, 'getUser').and.returnValue(of(mockUser));
    spyOn(bookService, 'getDeal').and.returnValue(of({}));

    service.listBooksWithData().subscribe(result => {
      expect(result.data.length).toBe(1);
      expect(result.data[0].writerInfo).toEqual(mockUser);
      expect(result.data[0].dealInfo).toBeNull();
      expect(bookService.listBooks).toHaveBeenCalled();
      expect(userService.getUser).toHaveBeenCalledWith('greedy-writer-url');
      expect(bookService.getDeal).not.toHaveBeenCalled();
      done();
    });
  })

});
