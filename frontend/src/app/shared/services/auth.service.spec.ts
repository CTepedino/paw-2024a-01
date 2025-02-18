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
    const userPromise = firstValueFrom(user$); // Convert observable to promise

    // Mock API response for index
    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    expect(reqIndex.request.method).toBe('GET');
    reqIndex.flush(mockIndex);

    // Mock API response for user
    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser!);
    expect(reqUser.request.method).toBe('GET');
    reqUser.flush(mockUser);

    // Ensure response matches expected user
    expect(await userPromise).toEqual(mockUser);
  });

  it('should return null when API responds with no logged user', async () => {
    service['loggedUserSubject'].next(undefined);
    const user$ = service.getLoggedUser();
    const userPromise = firstValueFrom(user$);

    // Mock API response for index (no logged user)
    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    expect(reqIndex.request.method).toBe('GET');
    reqIndex.flush({...mockIndex, loggedUser: null});

    // Ensure response matches expected null
    expect(await userPromise).toBeNull();
  });

  it('should return cached user if already set (avoiding API call)', async () => {
    service['loggedUserSubject'].next(mockUser); // Manually set cached user

    const user$ = service.getLoggedUser();
    const userPromise = firstValueFrom(user$);

    // Ensure it returns the cached user
    expect(await userPromise).toEqual(mockUser);
  });

  it('should return null when API request fails', async () => {
    const user$ = service.getLoggedUserFromApi();
    const userPromise = firstValueFrom(user$);

    // Simulate index API failure
    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    reqIndex.flush('Failed!', {status: 500, statusText: 'Internal Server Error'});

    // Ensure the method handles the error and returns null
    expect(await userPromise).toBeNull();
  });

  it('should always fetch the logged-in user from API (no caching)', async () => {
    service['loggedUserSubject'].next(mockUser); // Set a cached user
    const user$ = service.getLoggedUserFromApi();
    const userPromise = firstValueFrom(user$);

    // It should still make an API call despite the cached value
    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    expect(reqIndex.request.method).toBe('GET');
    reqIndex.flush(mockIndex);

    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser!);
    expect(reqUser.request.method).toBe('GET');
    reqUser.flush(mockUser);

    expect(await userPromise).toEqual(mockUser);
  });

  it('should reset the logged user and fetch updated user data', () => {
    // Set a mock user in the subject
    service['loggedUserSubject'].next(mockUser);

    // Call resetLoggedUser
    service.resetLoggedUser();

    // Ensure the user was first reset
    expect(service['loggedUserSubject'].value).toBeUndefined();

    // Expect an HTTP GET request to the user's `self` URL
    const req = httpTestingController.expectOne(mockUser.self!);
    expect(req.request.method).toBe('GET');

    // Respond with updated user data
    const updatedUser = { ...mockUser, firstName: 'Updated' };
    req.flush(updatedUser);

    // Ensure the subject was updated with the new user data
    expect(service['loggedUserSubject'].value).toEqual(updatedUser);
  });

  it('should log in the user and store tokens', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';
    const rememberMe = true;

    // Call login
    const login$ = service.login(email, password, rememberMe);
    const loginPromise = firstValueFrom(login$);

    // Expect a request with Basic Auth
    const reqLogin = httpTestingController.expectOne(service['baseUrl']);
    expect(reqLogin.request.method).toBe('GET');
    expect(reqLogin.request.headers.get('Authorization')).toBe('Basic ' + btoa(`${email}:${password}`));

    // Mock response with tokens
    const jwtToken = 'mock-jwt-token';
    const refreshToken = 'mock-refresh-token';
    reqLogin.flush(mockIndex, {
      headers: new HttpHeaders({
        Authorization: jwtToken,
        'X-Refresh-Token': refreshToken,
      }),
    });

    // Expect request to fetch logged user
    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser!);
    expect(reqUser.request.method).toBe('GET');
    reqUser.flush(mockUser);

    // Ensure the response contains the index data
    expect(await loginPromise).toEqual(mockIndex);

    // Verify tokens are stored correctly
    expect(localStorage.getItem('jwt')).toBe(jwtToken);
    expect(localStorage.getItem('refreshToken')).toBe(refreshToken);

    // Ensure loggedUserSubject was updated
    expect(service['loggedUserSubject'].value).toEqual(mockUser);
  });

  it('should log out the user and clear tokens', () => {
    // Set tokens and a logged-in user
    sessionStorage.setItem('jwt', 'test-jwt');
    sessionStorage.setItem('refreshToken', 'test-refresh');
    localStorage.setItem('jwt', 'test-jwt');
    localStorage.setItem('refreshToken', 'test-refresh');
    service['loggedUserSubject'].next(mockUser);

    // Call logout
    service.logout();

    // Expect tokens to be removed
    expect(sessionStorage.getItem('jwt')).toBeNull();
    expect(sessionStorage.getItem('refreshToken')).toBeNull();
    expect(localStorage.getItem('jwt')).toBeNull();
    expect(localStorage.getItem('refreshToken')).toBeNull();

    // Expect loggedUserSubject to be null
    expect(service['loggedUserSubject'].value).toBeNull();
  });

  it('should throw an error if no refresh token is found', () => {
    spyOn(service, 'getRefreshToken').and.returnValue(null);

    expect(() => service.refreshToken()).toThrowError('No refresh token');
  });

  it('should verify credentials and return true when the user is logged in', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';

    // Store previous tokens
    sessionStorage.setItem('jwt', 'old-jwt');
    sessionStorage.setItem('refreshToken', 'old-refresh');

    const verify$ = service.verifyCredentials(email, password);
    const verifyPromise = firstValueFrom(verify$);

    // Expect a request with Basic Auth
    const reqVerify = httpTestingController.expectOne(service['baseUrl']);
    expect(reqVerify.request.method).toBe('GET');
    expect(reqVerify.request.headers.get('Authorization')).toBe('Basic ' + btoa(`${email}:${password}`));

    // Mock a successful response with a logged user
    reqVerify.flush(mockIndex);

    // Expect the method to return true
    expect(await verifyPromise).toBeTrue();

    // Ensure previous tokens are restored
    expect(sessionStorage.getItem('jwt')).toBe('old-jwt');
    expect(sessionStorage.getItem('refreshToken')).toBe('old-refresh');
  });

  it('should return false if API response does not contain a logged user', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';

    const verify$ = service.verifyCredentials(email, password);
    const verifyPromise = firstValueFrom(verify$);

    // Expect API request
    const reqVerify = httpTestingController.expectOne(service['baseUrl']);
    expect(reqVerify.request.method).toBe('GET');

    // Mock response with no logged user
    reqVerify.flush({ ...mockIndex, loggedUser: null });

    // Expect method to return false
    expect(await verifyPromise).toBeFalse();
  });

  it('should return false when API request fails', async () => {
    const email = 'testuser@example.com';
    const password = 'password123';

    const verify$ = service.verifyCredentials(email, password);
    const verifyPromise = firstValueFrom(verify$);

    // Simulate an API failure
    const reqVerify = httpTestingController.expectOne(service['baseUrl']);
    reqVerify.flush('Error', { status: 500, statusText: 'Internal Server Error' });

    // Expect method to return false
    expect(await verifyPromise).toBeFalse();
  });
});
