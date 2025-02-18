import {TestBed} from '@angular/core/testing';

import {AuthService} from './auth.service';
import {provideHttpClient} from "@angular/common/http";
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
    const reqUser = httpTestingController.expectOne(mockIndex.loggedUser ?? '/api/default-user');
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

  it('should handle API errors gracefully', async () => {
    service['loggedUserSubject'].next(undefined);
    const user$ = service.getLoggedUser();
    const userPromise = firstValueFrom(user$);

    // Simulate index API failure
    const reqIndex = httpTestingController.expectOne(service['baseUrl']);
    reqIndex.error(new ProgressEvent('error'));

    try {
      await userPromise;
      fail('Expected an error to be thrown');
    } catch (error) {
      expect(error).toBeTruthy();
    }

    httpTestingController.verify();
  });
});
