import { TestBed } from '@angular/core/testing';
import {ActivatedRouteSnapshot, CanActivateFn, provideRouter, Router, RouterStateSnapshot} from '@angular/router';

import { numericIDGuard } from './numeric-id.guard';
import {BookService} from "../services/book.service";
import {AppComponent} from "../../app.component";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('numericIDGuard', () => {
  let router: Router;
  let route: ActivatedRouteSnapshot;
  let state: RouterStateSnapshot;

  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => numericIDGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BookService,
        Router,
        provideRouter([{path: ':id', component: AppComponent}]),
      ],
    });

    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1' } });
    state = { url: '/dashboard' } as RouterStateSnapshot;

  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });

  it('should allow if id is number', () => {
    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: '1235' } });


    const result = executeGuard(route, state);
    expect(result).toBeTrue();
  })

  it('should fail if id is not number', () => {
    route = new ActivatedRouteSnapshot();
    Object.defineProperty(route, 'params', { value: { id: 'khkjh' } });

    const result = executeGuard(route, state);
    expect(result).toBeFalse();
  })
});
