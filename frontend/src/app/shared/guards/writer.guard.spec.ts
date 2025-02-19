import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { writerGuard } from './writer.guard';
import {UserService} from "../services/user.service";
import {idIsWriterGuard} from "./id-is-writer.guard";
import {BookService} from "../services/book.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {Observable, of} from "rxjs";
import {UserRoles} from "../model/user/userRoles";
import {AuthService} from "../services/auth.service";

describe('writerGuard', () => {
  let authService: AuthService;
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  const executeGuard: CanActivateFn = (...guardParameters) =>
      TestBed.runInInjectionContext(() => writerGuard(...guardParameters));

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

    const parent = new ActivatedRouteSnapshot();
    Object.defineProperty(parent, 'params', { value: { id: '1' } });
    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'parent',{value: parent});
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow when user has writer role', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1, self: 'me', roles: [UserRoles.WRITER]}));

    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(value => {
      expect(value).toBeTrue();
      done();
    });
  });

  it('should fail when logged user has not writer role', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1, self: 'me', roles: [UserRoles.READER]}));
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalled();
      done();
    });
  });

});
