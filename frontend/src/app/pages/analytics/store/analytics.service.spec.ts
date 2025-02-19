import {BookService} from "../../../shared/services/book.service";
import {TestBed} from "@angular/core/testing";
import {Analytics, AnalyticsService, BookWithAnalytics} from "./analytics.service";
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {Book} from "../../../shared/model/book/book";
import {of} from "rxjs";
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";

describe('AnalyticsService', () => {
	let service: AnalyticsService;
	let authService: jasmine.SpyObj<AuthService>;
	let userService: jasmine.SpyObj<UserService>;
	let bookService: jasmine.SpyObj<BookService>;

	const mockUser = {
		id: 1,
		orderCount: 5,
		salesTotal: 100,
		currentMonthlyAnalytics: 'monthly-analytics/0001-01',
		self: 'users/1'
	};
	const mockPaginatedBooks: PaginatedContent<Book> = {
		data: [{id: 1, title: 'Book 1', orderCount: 2, salesTotal: 50, self: 'books/1'} as Book],
		pagination: {totalCount: 1, pageCount: 1}
	};

	beforeEach(() => {
		TestBed.configureTestingModule({
			providers: [
				provideHttpClient(),
				provideHttpClientTesting(),
				AnalyticsService,
				{provide: AuthService, useValue: jasmine.createSpyObj('AuthService', ['getLoggedUser'])},
				{provide: UserService, useValue: jasmine.createSpyObj('UserService', ['getWriterMonthlyAnalytics', 'getWriterMonthlyAnalyticsFromWriter'])},
				{provide: BookService, useValue: jasmine.createSpyObj('BookService', ['listBooks', 'getBookMonthlyAnalyticsFromBook'])}
			]
		});

		service = TestBed.inject(AnalyticsService);
		authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
		userService = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
		bookService = TestBed.inject(BookService) as jasmine.SpyObj<BookService>;
	});


	describe('getTotal', () => {
		it('should return Analytics from logged user data', (done) => {
			authService.getLoggedUser.and.returnValue(of(mockUser));

			service.getTotal().subscribe((result) => {
				expect(result).toEqual(new Analytics(mockUser.orderCount, mockUser.salesTotal));
				done();
			});
		});
	});

	describe('getCurrentMonthTotal', () => {
		it('should fetch and return current month analytics', (done) => {
			authService.getLoggedUser.and.returnValue(of(mockUser));
			userService.getWriterMonthlyAnalytics.and.returnValue(of({ orderCount: 3, salesTotal: 60 }));

			service.getCurrentMonthTotal().subscribe((result) => {
				expect(userService.getWriterMonthlyAnalytics).toHaveBeenCalledWith('monthly-analytics/0001-01');
				expect(result).toEqual(new Analytics(3, 60));
				done();
			});
		});
	});

	describe('getMonthlyTotal', () => {
		it('should fetch monthly analytics for a month', (done) => {
			authService.getLoggedUser.and.returnValue(of(mockUser));
			userService.getWriterMonthlyAnalyticsFromWriter.and.returnValue(of({ orderCount: 4, salesTotal: 80 }));

			service.getMonthlyTotal(2024, 2).subscribe((result) => {
				expect(userService.getWriterMonthlyAnalyticsFromWriter).toHaveBeenCalledWith('users/1', '2024-02');
				expect(result).toEqual(new Analytics(4, 80));
				done();
			});
		});
	});

	describe('getBooksWithAnalytics', () => {
		it('should retrieve books with analytics data', (done) => {
			authService.getLoggedUser.and.returnValue(of(mockUser));
			bookService.listBooks.and.returnValue(of(mockPaginatedBooks));

			service.getBooksWithAnalytics(1, 10).subscribe((result) => {
				expect(bookService.listBooks).toHaveBeenCalledWith({ writer_id: 1, page: 1, size: 10 });
				expect(result.data.length).toBe(1);
				expect(result.data[0]).toEqual(new BookWithAnalytics(mockPaginatedBooks.data[0], new Analytics(2, 50)));
				done();
			});
		});
	});

	describe('getBooksWithMonthlyAnalytics', () => {
		it('should return empty array if no books', (done) => {
			authService.getLoggedUser.and.returnValue(of(mockUser));
			bookService.listBooks.and.returnValue(of({ data: [], pagination: mockPaginatedBooks.pagination }));

			service.getBooksWithMonthlyAnalytics(2024, 2, 1, 10).subscribe((result) => {
				expect(result).toEqual({ data: [], pagination: mockPaginatedBooks.pagination });
				done();
			});
		});

		it('should fetch monthly analytics for each book', (done) => {
			authService.getLoggedUser.and.returnValue(of(mockUser));
			bookService.listBooks.and.returnValue(of(mockPaginatedBooks));
			bookService.getBookMonthlyAnalyticsFromBook.and.returnValue(of({ orderCount: 2, salesTotal: 50 }));

			service.getBooksWithMonthlyAnalytics(2024, 2, 1, 10).subscribe((result) => {
				expect(bookService.getBookMonthlyAnalyticsFromBook).toHaveBeenCalledWith('books/1', '2024-02');
				expect(result.data[0]).toEqual(new BookWithAnalytics(mockPaginatedBooks.data[0], new Analytics(2, 50)));
				done();
			});
		});
	});
});