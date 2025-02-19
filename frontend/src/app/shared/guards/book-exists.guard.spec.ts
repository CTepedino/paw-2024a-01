import {TestBed} from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import {bookExistsGuard} from './book-exists.guard';
import {BookService} from "../services/book.service";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {Observable, of, throwError} from "rxjs";
import {AppComponent} from "../../app.component";

describe('bookExistsGuard', () => {
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

    bookService = TestBed.inject(BookService);
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => bookExistsGuard(...guardParameters));


  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when the book exists', (done) => {
    spyOn(bookService, 'getBookById').and.returnValue(of({id: 1, title: 'Mock Bok'}));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(value => {
      expect(value).toBeTrue();
      expect(bookService.getBookById).toHaveBeenCalledWith('1');
      done();
    });
  });

  it('should 404 when the book does not exist', () => {
    spyOn(bookService, 'getBookById').and.returnValue(throwError(() => new Error('Book not found')));
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/404']);
      expect(bookService.getBookById).toHaveBeenCalledWith('1');
    });
  });
});
