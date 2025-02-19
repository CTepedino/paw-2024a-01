import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { loggedInGuard } from './logged-in.guard';
import {BookService} from "../services/book.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {bookExistsGuard} from "./book-exists.guard";
import {Observable, of, throwError} from "rxjs";
import {AuthService} from "../services/auth.service";

describe('loggedInGuard', () => {
  let authService: AuthService;
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;


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
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => loggedInGuard(...guardParameters));


  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when logged', () => {
    spyOn(authService, 'getJwtToken').and.returnValue('true');


    const result = executeGuard(route, state);
    expect(result).toBeTrue();
  });

  it('should fail when unlogged', () => {
    spyOn(authService, 'getJwtToken').and.returnValue(null);
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);
    expect(result).toBeFalse();
    expect(router.navigate).toHaveBeenCalled();

  });
});
