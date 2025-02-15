import { TestBed } from '@angular/core/testing';

import { OrderWithDataService } from './order-with-data.service';

describe('OrderWithDataService', () => {
  let service: OrderWithDataService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrderWithDataService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
