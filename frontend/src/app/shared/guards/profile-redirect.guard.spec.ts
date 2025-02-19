import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { profileRedirectGuard } from './profile-redirect.guard';
import {AuthService} from "../services/auth.service";
import {inject} from "@angular/core";
import {Observable, of} from "rxjs";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('profileRedirectGuard', () => {
  let authService: AuthService;
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => profileRedirectGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          AuthService,
          Router,
          provideRouter([{path: ':id', component: AppComponent}]),
          provideHttpClient(),
          provideHttpClientTesting(),
      ]
    });

    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should redirect to profile if logged', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of({id: 1, self: 'me'}));
    spyOn(router, 'navigate');

    const result = executeGuard(route, state);


    (result as Observable<any>).subscribe(value => {
      expect(value).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/profile/1'])
      done();
    });
  })

  it ('should redirect to login if unlogged', (done) => {
    spyOn(authService, 'getLoggedUserFromApi').and.returnValue(of(null));
    spyOn(router, 'navigate');

    const result = executeGuard(route, state);


    (result as Observable<any>).subscribe(value => {
      expect(value).toBeFalse();
      expect(router.navigate).toHaveBeenCalledWith(['/login'])
      done();
    });
  })
});
