import {TestBed} from '@angular/core/testing';

import {provideHttpClientTesting} from "@angular/common/http/testing";
import {provideHttpClient} from "@angular/common/http";
import {BookWithDataService} from "../../../shared/services/book-with-data.service";
import {UserService} from "../../../shared/services/user.service";
import {AuthService} from "../../../shared/services/auth.service";
import {ProfileDisplayInfo, UserProfileService} from "./user-profile.service";
import {User} from "../../../shared/model/user/user";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {BookWithData} from "../../../shared/model/book/bookWithData";
import {of} from "rxjs";

describe('GenreService', () => {
	let service: UserProfileService;
	let authService: jasmine.SpyObj<AuthService>;
	let userService: jasmine.SpyObj<UserService>;
	let bookWithDataService: jasmine.SpyObj<BookWithDataService>;


	const mockUser: User = { id: 1, firstName: 'johnny', lastName: 'test' } as User;
	const mockPaginatedBooks: PaginatedContent<BookWithData> = {
		data: [{ id: 1, title: 'Book' } as BookWithData],
		pagination: { totalCount: 1, pageCount: 1 }
	};


	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [
				UserProfileService,
				{ provide: AuthService, useValue: jasmine.createSpyObj('AuthService', ['getLoggedUser']) },
				{ provide: UserService, useValue: jasmine.createSpyObj('UserService', ['getUserById', 'getRecommendations', 'getWishlist']) },
				{ provide: BookWithDataService, useValue: jasmine.createSpyObj('BookWithDataService', ['listBooksWithData', 'getBookWithData']) },
				provideHttpClient(),
				provideHttpClientTesting(),
			],
		});
		service = TestBed.inject(UserProfileService);
		authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
		userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
		bookWithDataService = TestBed.inject(BookWithDataService) as jasmine.SpyObj<BookWithDataService>;
	});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	describe('getUser', () => {
		it('should return user with isOwner true if logged user matches', (done) => {
			userService.getUserById.and.returnValue(of(mockUser));
			authService.getLoggedUser.and.returnValue(of({ id: 1 }));

			service.getUser(1).subscribe((result) => {
				expect(result).toEqual(new ProfileDisplayInfo(mockUser, true));
				done();
			});
		});

		it('should return user with isOwner false if logged user not user', (done) => {
			userService.getUserById.and.returnValue(of(mockUser));
			authService.getLoggedUser.and.returnValue(of({id: 2}));

			service.getUser(1).subscribe((result) => {
				expect(result).toEqual(new ProfileDisplayInfo(mockUser, false));
				done();
			});
		});

		it('should return user with isOwner false if no logged user', (done) => {
			userService.getUserById.and.returnValue(of(mockUser));
			authService.getLoggedUser.and.returnValue(of(null));

			service.getUser(1).subscribe((result) => {
				expect(result).toEqual(new ProfileDisplayInfo(mockUser, false));
				done();
			});
		});
	});

	describe('getRecommendations', () => {
		it('should return empty array if no recommendations', (done) => {
			userService.getRecommendations.and.returnValue(of({ data: [], pagination: mockPaginatedBooks.pagination }));

			service.getRecommendations(1, 1, 10).subscribe((result) => {
				expect(result).toEqual({ data: [], pagination: mockPaginatedBooks.pagination });
				done();
			});
		});

		it('should fetch books from recommendations', (done) => {
			userService.getRecommendations.and.returnValue(of({
				data: [{ bookId: 1 }],
				pagination: mockPaginatedBooks.pagination
			}));
			bookWithDataService.getBookWithData.and.returnValue(of(mockPaginatedBooks.data[0]));

			service.getRecommendations(1, 1, 10).subscribe((result) => {
				expect(result.data.length).toBe(1);
				expect(bookWithDataService.getBookWithData).toHaveBeenCalledWith(1);
				done();
			});
		});
	});

	describe('getWishlist', () => {
		it('should return empty array if no wishlist', (done) => {
			userService.getWishlist.and.returnValue(of({ data: [], pagination: mockPaginatedBooks.pagination }));

			service.getWishlist(1, 1, 10).subscribe((result) => {
				expect(result).toEqual({ data: [], pagination: mockPaginatedBooks.pagination });
				done();
			});
		});

		it('should fetch books from wishlist', (done) => {
			userService.getWishlist.and.returnValue(of({
				data: [{ bookId: 1 }],
				pagination: mockPaginatedBooks.pagination
			}));
			bookWithDataService.getBookWithData.and.returnValue(of(mockPaginatedBooks.data[0]));

			service.getWishlist(1, 1, 10).subscribe((result) => {
				expect(result.data.length).toBe(1);
				expect(bookWithDataService.getBookWithData).toHaveBeenCalledWith(1);
				done();
			});
		});
	});
});