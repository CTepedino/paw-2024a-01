import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { canValidateCodeGuard } from './can-validate-code.guard';
import {BookService} from "../services/book.service";
import {AuthService} from "../services/auth.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {Observable, of, throwError} from "rxjs";

describe('canValidateCodeGuard', () => {
  let authService: AuthService;
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;


  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => canValidateCodeGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        AuthService,
        Router,
        provideRouter([{ path:"", component: AppComponent}]),
        provideHttpClient(),
        provideHttpClientTesting(),
      ],
    });

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'queryParams', { value: { id: '1', code: '12345' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should fail when the code is invalid', () => {
    spyOn(authService, 'validateCode').and.returnValue(of({self: '', users: '', genres: '', books: '', orders: '', questions: '', resetCodes: '', validationCodes: '', loggedUser: null}));
    spyOn(authService, 'getJwtToken').and.returnValue(null);
    spyOn(router, 'navigate');


    const result = executeGuard(route, state);

    (result as Observable<any>).subscribe(result => {
      expect(result).toBeFalse();
      expect(router.navigate).toHaveBeenCalled();
    });
  });

  it('should fail when the query params are invalid', () => {
    spyOn(authService, 'validateCode').and.returnValue(of({self: '', users: '', genres: '', books: '', orders: '', questions: '', resetCodes: '', validationCodes: '', loggedUser: null}));
    spyOn(authService, 'getJwtToken').and.returnValue('true');

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'queryParams', { value: { id: '1' } });

    expect(executeGuard(route, state)).toBeFalse();
  });

});
