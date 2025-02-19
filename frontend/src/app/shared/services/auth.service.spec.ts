import {TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {HttpHeaders, provideHttpClient} from "@angular/common/http";
import {HttpTestingController, provideHttpClientTesting} from "@angular/common/http/testing";
import {Index} from "../model";
import {User} from "../model/user/user";
import {WriterCategory} from "../model/user/writerCategory";
import {UserRoles} from "../model/user/userRoles";
import {firstValueFrom} from "rxjs";

describe('AuthService', () => {
  let service: AuthService;
  let httpTestingController: HttpTestingController;
  let mockIndex: Index;
  let mockUser: User;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
          AuthService,
          provideHttpClient(),
          provideHttpClientTesting(),
      ],
    });

    service = TestBed.inject(AuthService);
    httpTestingController = TestBed.inject(HttpTestingController);

    mockIndex = {
      self: '/api',
      users: '/api/users',
      genres: '/api/genres',
      books: '/api/books',
      orders: '/api/orders',
      questions: '/api/questions',
      resetCodes: '/api/resetCodes',
      validationCodes: '/api/validationCodes',
      loggedUser: '/api/users/1',
    };

    mockUser = {
      id: 1,
      email: 'testuser@example.com',
      firstName: 'Test',
      lastName: 'User',
      writerCategory: WriterCategory.BRONZE,
      roles: [UserRoles.READER],
      orderCount: 5,
      salesTotal: 2000,
      profilePicture: '/images/profile.jpg',
      self: '/api/users/1',
    };
  });

  afterEach(() => {
    TestBed.inject(HttpTestingController).verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a logged-in user when API responds with user data', async() => {
    service['loggedUserSubject'].next(undefined);
    const user$ = service.getLoggedUser();
    const userPromise = firstValueFrom(user$);

    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    expect(reqIndex.request.method).toBe('GET');
    reqIndex.flush(mockIndex);

    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser!);
    expect(reqUser.request.method).toBe('GET');
    reqUser.flush(mockUser);

    expect(await userPromise).toEqual(mockUser);
  });

  it('should return null when API responds with no logged user', async () => {
    service['loggedUserSubject'].next(undefined);
    const user$ = service.getLoggedUser();
    const userPromise = firstValueFrom(user$);

    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    expect(reqIndex.request.method).toBe('GET');
    reqIndex.flush({...mockIndex, loggedUser: null});

    expect(await userPromise).toBeNull();
  });

  it('should return cached user if already set (avoiding API call)', async () => {
    service['loggedUserSubject'].next(mockUser);

    const user$ = service.getLoggedUser();
    const userPromise = firstValueFrom(user$);

    expect(await userPromise).toEqual(mockUser);
  });

  it('should return null when API request fails', async () => {
    const user$ = service.getLoggedUserFromApi();
    const userPromise = firstValueFrom(user$);

    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    reqIndex.flush('Failed!', {status: 500, statusText: 'Internal Server Error'});

    expect(await userPromise).toBeNull();
  });

  it('should always fetch the logged-in user from API (no caching)', async () => {
    service['loggedUserSubject'].next(mockUser);
    const user$ = service.getLoggedUserFromApi();
    const userPromise = firstValueFrom(user$);

    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    expect(reqIndex.request.method).toBe('GET');
    reqIndex.flush(mockIndex);

    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser!);
    expect(reqUser.request.method).toBe('GET');
    reqUser.flush(mockUser);

    expect(await userPromise).toEqual(mockUser);
  });

  it('should reset the logged user and fetch updated user data', () => {
    service['loggedUserSubject'].next(mockUser);

    service.resetLoggedUser();

    expect(service['loggedUserSubject'].value).toBeUndefined();

    const req = httpTestingController.expectOne(mockUser.self!);
    expect(req.request.method).toBe('GET');

    const updatedUser = { ...mockUser, firstName: 'Updated' };
    req.flush(updatedUser);

    expect(service['loggedUserSubject'].value).toEqual(updatedUser);
  });

  it('should log in the user and store tokens', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';
    const rememberMe = true;

    const login$ = service.login(email, password, rememberMe);
    const loginPromise = firstValueFrom(login$);

    const reqLogin = httpTestingController.expectOne(service['baseUrl']);
    expect(reqLogin.request.method).toBe('GET');
    expect(reqLogin.request.headers.get('Authorization')).toBe('Basic ' + btoa(`${email}:${password}`));

    const jwtToken = 'mock-jwt-token';
    const refreshToken = 'mock-refresh-token';
    reqLogin.flush(mockIndex, {
      headers: new HttpHeaders({
        Authorization: jwtToken,
        'X-Refresh-Token': refreshToken,
      }),
    });

    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser!);
    expect(reqUser.request.method).toBe('GET');
    reqUser.flush(mockUser);

    expect(await loginPromise).toEqual(mockIndex);

    expect(localStorage.getItem('jwt')).toBe(jwtToken);
    expect(localStorage.getItem('refreshToken')).toBe(refreshToken);

    expect(service['loggedUserSubject'].value).toEqual(mockUser);
  });

  it('should log out the user and clear tokens', () => {
    sessionStorage.setItem('jwt', 'test-jwt');
    sessionStorage.setItem('refreshToken', 'test-refresh');
    localStorage.setItem('jwt', 'test-jwt');
    localStorage.setItem('refreshToken', 'test-refresh');
    service['loggedUserSubject'].next(mockUser);

    service.logout();

    expect(sessionStorage.getItem('jwt')).toBeNull();
    expect(sessionStorage.getItem('refreshToken')).toBeNull();
    expect(localStorage.getItem('jwt')).toBeNull();
    expect(localStorage.getItem('refreshToken')).toBeNull();

    expect(service['loggedUserSubject'].value).toBeNull();
  });

  it('should throw an error if no refresh token is found', () => {
    spyOn(service, 'getRefreshToken').and.returnValue(null);

    expect(() => service.refreshToken()).toThrowError('No refresh token');
  });

  it('should verify credentials and return true when the user is logged in', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';

    sessionStorage.setItem('jwt', 'old-jwt');
    sessionStorage.setItem('refreshToken', 'old-refresh');

    const verify$ = service.verifyCredentials(email, password);
    const verifyPromise = firstValueFrom(verify$);

    const reqVerify = httpTestingController.expectOne(service['baseUrl']);
    expect(reqVerify.request.method).toBe('GET');
    expect(reqVerify.request.headers.get('Authorization')).toBe('Basic ' + btoa(`${email}:${password}`));

    reqVerify.flush(mockIndex);

    sessionStorage.setItem('jwt', 'old-jwt');
    sessionStorage.setItem('refreshToken', 'old-refresh');

    expect(await verifyPromise).toBeTrue();

    expect(sessionStorage.getItem('jwt')).toBe('old-jwt');
    expect(sessionStorage.getItem('refreshToken')).toBe('old-refresh');
  });

  it('should return false if API response does not contain a logged user', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';

    const verify$ = service.verifyCredentials(email, password);
    const verifyPromise = firstValueFrom(verify$);

    const reqVerify = httpTestingController.expectOne(service['baseUrl']);
    expect(reqVerify.request.method).toBe('GET');

    reqVerify.flush({ ...mockIndex, loggedUser: null });

    expect(await verifyPromise).toBeFalse();
  });

  it('should return false when API request fails', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';

    const verify$ = service.verifyCredentials(email, password);
    const verifyPromise = firstValueFrom(verify$);

    const reqVerify = httpTestingController.expectOne(service['baseUrl']);
    reqVerify.flush('Error', { status: 500, statusText: 'Internal Server Error' });

    expect(await verifyPromise).toBeFalse();
  });
});
