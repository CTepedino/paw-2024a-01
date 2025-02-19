import { TestBed } from '@angular/core/testing';

import { OrderService } from './order.service';
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {AuthService} from "./auth.service";
import {provideHttpClient} from "@angular/common/http";
import {OrderSearchQuery} from "../model/order/orderSearchQuery";

describe('OrderService', () => {
  let service: OrderService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });
    service = TestBed.inject(OrderService);
    httpTestingController = TestBed.inject(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should set default page to 10 if not provided', () => {
    const mockResponse = { body: [] };

    service.listOrders({} as OrderSearchQuery).subscribe();

    const req = httpTestingController.expectOne((req) =>
        req.url.includes(service['apiURL'])
    );
    expect(req.request.method).toBe('GET');
    expect(req.request.params.get('page')).toBe('10');

    req.flush(mockResponse);
  });

  it('should send provided query parameters', () => {
    const mockQuery: OrderSearchQuery = { page: 2, size: 5};

    service.listOrders(mockQuery).subscribe();

    const req = httpTestingController.expectOne((req) =>
        req.url.includes(service['apiURL'])
    );
    expect(req.request.params.get('page')).toBe('2');
    expect(req.request.params.get('size')).toBe('5');

    req.flush([]);
  });
});
