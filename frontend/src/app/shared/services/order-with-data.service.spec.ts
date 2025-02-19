import { TestBed } from '@angular/core/testing';

import { OrderWithDataService } from './order-with-data.service';
import {OrderService} from "./order.service";
import {UserService} from "./user.service";
import {BookService} from "./book.service";
import {AuthService} from "./auth.service";
import {Order} from "../model/order/order";
import {PaginatedContent} from "../model/paginatedContent";
import {Book} from "../model/book/book";
import {User} from "../model/user/user";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {of} from "rxjs";

describe('OrderWithDataService', () => {

  let service: OrderWithDataService;
  let orderService: OrderService;
  let userService: UserService;
  let bookService: BookService;
  let authService: AuthService;

  const mockOrder: Order = { orderId: 1, buyer: 'buyer-url', seller: 'seller-url', book: 'book-url'};
  const mockBuyer: User = { id: 1, firstName: 'Yo',  lastName: 'Compro' };
  const mockSeller: User = { id: 2, firstName: 'Yo',  lastName: 'Vendo' };
  const mockBook: Book = { id: 1, title: 'Test Book', writer: 'writer-url'};
  const mockPaginatedOrders: PaginatedContent<Order> = {
    data: [mockOrder],
    pagination: { totalCount: 1, pageCount: 1 }
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          OrderWithDataService,
          OrderService,
          UserService,
          BookService,
          AuthService,
          provideHttpClient(),
          provideHttpClientTesting()
      ]
    });

    service = TestBed.inject(OrderWithDataService);
    orderService = TestBed.inject(OrderService);
    userService = TestBed.inject(UserService);
    bookService = TestBed.inject(BookService);
    authService = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return sales and buyer info', (done) => {
    spyOn(orderService, 'listOrders').and.returnValue(of(mockPaginatedOrders));
    spyOn(userService, 'getUser').and.returnValue(of(mockBuyer));
    spyOn(bookService, 'getBook').and.returnValue(of(mockBook));
    spyOn(authService, 'getLoggedUser').and.returnValue(of(mockSeller));

    service.getSales({}).subscribe(result => {
      expect(result.data.length).toBe(1);
      expect(result.data[0].bookInfo).toEqual(mockBook);
      expect(result.data[0].buyerInfo).toEqual(mockBuyer);
      expect(orderService.listOrders).toHaveBeenCalled();
      expect(userService.getUser).toHaveBeenCalledWith('buyer-url');
      expect(bookService.getBook).toHaveBeenCalledWith('book-url');
      expect(authService.getLoggedUser).toHaveBeenCalledWith();
      done();
    });
  });

  it('should return purchases and seller info', (done) => {
    spyOn(orderService, 'listOrders').and.returnValue(of(mockPaginatedOrders));
    spyOn(userService, 'getUser').and.returnValue(of(mockSeller));
    spyOn(bookService, 'getBook').and.returnValue(of(mockBook));
    spyOn(authService, 'getLoggedUser').and.returnValue(of(mockBuyer));

    service.getPurchases({}).subscribe(result => {
      expect(result.data.length).toBe(1);
      expect(result.data[0].bookInfo).toEqual(mockBook);
      expect(result.data[0].writerInfo).toEqual(mockSeller);
      expect(orderService.listOrders).toHaveBeenCalled();
      expect(userService.getUser).toHaveBeenCalledWith('seller-url');
      expect(bookService.getBook).toHaveBeenCalledWith('book-url');
      expect(authService.getLoggedUser).toHaveBeenCalledWith();
      done();
    });
  });
});
