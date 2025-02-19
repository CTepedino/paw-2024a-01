import { TestBed } from '@angular/core/testing';

import { OrderWithDataService } from './order-with-data.service';
import {OrderService} from "./order.service";
import {UserService} from "./user.service";
import {BookService} from "./book.service";
import {AuthService} from "./auth.service";
import {User} from "../model/user/user";
import {OrderSearchQuery} from "../model/order/orderSearchQuery";
import {PaginatedContent} from "../model/paginatedContent";
import {OrderWithData} from "../model/order/orderWithData";
import {of} from "rxjs";
import {Order} from "../model/order/order";
import {Book} from "../model/book/book";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('OrderWithDataService', () => {
  let service: OrderWithDataService;
  let mockOrderService: jasmine.SpyObj<OrderService>;
  let mockUserService: jasmine.SpyObj<UserService>;
  let mockBookService: jasmine.SpyObj<BookService>;
  let mockAuthService: jasmine.SpyObj<AuthService>;

  beforeEach(() => {
    mockOrderService = jasmine.createSpyObj<OrderService>('OrderService', ['listOrders']);
    mockUserService = jasmine.createSpyObj<UserService>('UserService', ['getUser']);
    mockBookService = jasmine.createSpyObj<BookService>('BookService', ['getBook']);
    mockAuthService = jasmine.createSpyObj<AuthService>('AuthService', ['getLoggedUser']);

    TestBed.configureTestingModule({
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
        OrderWithDataService,
        { provide: OrderService, useValue: mockOrderService },
        { provide: UserService, useValue: mockUserService },
        { provide: BookService, useValue: mockBookService },
        { provide: AuthService, useValue: mockAuthService }
      ],
    });

    service = TestBed.inject(OrderWithDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

});
