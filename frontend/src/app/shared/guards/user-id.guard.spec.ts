import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { userIdGuard } from './user-id.guard';
import {BookService} from "../services/book.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {bookExistsGuard} from "./book-exists.guard";
import {Observable, of, throwError} from "rxjs";
import {AuthService} from "../services/auth.service";

describe('userIdGuard', () => {
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
    const parent = new ActivatedRouteSnapshot();
    Object.defineProperty(parent, 'params', { value: { id: '1' } });
    Object.defineProperty(route, 'parent', { value: parent });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => userIdGuard(...guardParameters));


  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when the user id is the logged users one', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1}));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should fail when the user id is not from the logged user', () => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 2}));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
    });
  });

  it('should fail when the user is unlogged', () => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of(null));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
    });
  });
});
