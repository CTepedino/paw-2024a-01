import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { userExistsGuard } from './user-exists.guard';
import {BookService} from "../services/book.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {bookExistsGuard} from "./book-exists.guard";
import {Observable, of, throwError} from "rxjs";
import {UserService} from "../services/user.service";

describe('userExistsGuard', () => {
  let userService: UserService;
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;


  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        UserService,
        Router,
        provideRouter([{path: ':id', component: AppComponent}]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    userService = TestBed.inject(UserService);
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => userExistsGuard(...guardParameters));


  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when the user exists', (done) => {
    spyOn(userService, 'getUserById').and.returnValue(of({id: 1, title: 'Mock Bok'}));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should 404 when the user does not exist', () => {
    spyOn(userService, 'getUserById').and.returnValue(throwError(() => new Error('Book not found')));
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/404']);
    });
  });
});
