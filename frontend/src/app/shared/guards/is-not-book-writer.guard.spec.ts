import {TestBed} from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import {isNotBookWriterGuard} from './is-not-book-writer.guard';
import {AuthService} from "../services/auth.service";
import {BookService} from "../services/book.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {Observable, of} from "rxjs";

describe('isNotBookWriterGuard', () => {
  let authService: AuthService;
  let bookService: BookService;
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
    bookService = TestBed.inject(BookService);
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => isNotBookWriterGuard(...guardParameters));


  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when logged user is not writer', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1, self: 'me'}));
    spyOn(bookService, 'getBookById').and.returnValue(of({id: 2, title: 'Mock Bok', writer: 'not-me'}));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(value => {
      expect(value).toBeTrue();
      expect(bookService.getBookById).toHaveBeenCalledWith(1);
      done();
    });
  });

  it('should fail when logged user is writer', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1, self: 'me'}));
    spyOn(bookService, 'getBookById').and.returnValue(of({id: 2, title: 'Mock Bok', writer: 'me'}));
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalled();
      expect(bookService.getBookById).toHaveBeenCalledWith(1);
      done();
    });
  });

  it('should fail when logged user is null', () => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of(null));
    spyOn(bookService, 'getBookById').and.returnValue(of({id: 2, title: 'Mock Bok', writer: 'maybe-me'}));
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalled();
      expect(bookService.getBookById).toHaveBeenCalledWith(1);
    });
  });
});
