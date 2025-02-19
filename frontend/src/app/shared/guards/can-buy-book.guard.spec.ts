import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { canBuyBookGuard } from './can-buy-book.guard';
import {BookService} from "../services/book.service";
import {OrderService} from "../services/order.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {Observable, of, throwError} from "rxjs";
import {authInterceptor} from "../interceptors/auth.interceptor";
import {AuthService} from "../services/auth.service";

describe('canBuyBookGuard', () => {
  let authService: AuthService;
  let orderService: OrderService;
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canBuyBookGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BookService,
        Router,
        provideRouter([{path: ':id', component: AppComponent}]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    authService = TestBed.inject(AuthService);
    orderService = TestBed.inject(OrderService);
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when user is logged and has not bought', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1, }));
    spyOn(orderService, 'listOrders').and.returnValue(of({
      pagination: {totalCount: 0, pageCount: 1},
      data: []
    }));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should fail when user is not logged', () => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of(null));
    spyOn(router, 'navigate');

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalled();
    });
  });

  it('should fail when user has an order for book', () => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of(null));
    spyOn(orderService, 'listOrders').and.returnValue(of({
      pagination: {totalCount: 1, pageCount: 1},
      data: []
    }));
    spyOn(router, 'navigate');

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalled();
    });
  });

});
