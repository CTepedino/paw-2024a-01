import {TestBed} from '@angular/core/testing';
import {BookDetailsService} from './book-details.service';
import {AuthService} from '../../../shared/services/auth.service';
import {BookService} from '../../../shared/services/book.service';
import {UserService} from '../../../shared/services/user.service';
import {provideHttpClient} from "@angular/common/http";
import {provideHttpClientTesting} from "@angular/common/http/testing";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {ReviewWithInfo} from "../../../shared/model/review/reviewWithInfo";
import {of} from "rxjs";
import {ReviewSearchQuery} from "../../../shared/model/review/reviewSearchQuery";


describe('BookDetailsService', () => {
	let service: BookDetailsService;
	let bookServiceSpy: jasmine.SpyObj<BookService>;
	let authServiceSpy: jasmine.SpyObj<AuthService>;
	let userServiceSpy: jasmine.SpyObj<UserService>;

		beforeEach(() => {
			authServiceSpy = jasmine.createSpyObj('AuthService', ['getLoggedUser']);
			userServiceSpy = jasmine.createSpyObj('UserService', [
				'postRecommendation',
				'deleteRecommendation',
				'postWishlistItem',
				'deleteWishlistItem'
			]);

			TestBed.configureTestingModule({
				providers: [
					provideHttpClient(),
					provideHttpClientTesting(),
					BookDetailsService,
					{ provide: AuthService, useValue: authServiceSpy },
					{ provide: UserService, useValue: userServiceSpy },
					{ provide: BookService, useValue: { listReviews: jasmine.createSpy('listReviews').and.returnValue(of({ data: [], pagination: {} }))}},
				]
			});
			service = TestBed.inject(BookDetailsService);
			authServiceSpy.getLoggedUser.and.returnValue(of({ self: '/user/1' }));
			bookServiceSpy = TestBed.inject(BookService) as jasmine.SpyObj<BookService>;
			userServiceSpy = TestBed.inject(UserService) as jasmine.SpyObj<UserService>;
		});

	it('should be created', () => {
		expect(service).toBeTruthy();
	});

	it('should return empty data when no reviews are found', (done) => {
		const mockBook = { self: '/books/1', writerInfo: {} };
		const emptyReviews: PaginatedContent<ReviewWithInfo> = {
			data: [],
			pagination: { totalCount: 2, pageCount: 1 },
		};

		spyOn(service, 'getBook').and.returnValue(of(mockBook));
		bookServiceSpy.listReviews.and.returnValue(of(emptyReviews));

		const query: ReviewSearchQuery = { page: 1, size: 10 };

		service.getReviews(1, query).subscribe((result) => {
			expect(service.getBook).toHaveBeenCalledWith(1);
			expect(bookServiceSpy.listReviews).toHaveBeenCalledWith('/books/1', query);
			expect(result.data).toEqual([]);
			done();
		});
	});

	describe('toggleRecommend', () => {
		it('should call postRecommendation when add is true', () => {
			userServiceSpy.postRecommendation.and.returnValue(of(void 0));
			service.toggleRecommend(1, true);
			expect(authServiceSpy.getLoggedUser).toHaveBeenCalled();
			expect(userServiceSpy.postRecommendation).toHaveBeenCalledWith('/user/1', 1);
		});

		it('should call deleteRecommendation when add is false', () => {
			userServiceSpy.deleteRecommendation.and.returnValue(of(void 0));
			service.toggleRecommend(1, false);
			expect(authServiceSpy.getLoggedUser).toHaveBeenCalled();
			expect(userServiceSpy.deleteRecommendation).toHaveBeenCalledWith('/user/1', 1);
		});
	});

	describe('toggleWishlist', () => {
		it('should call postWishlistItem when add is true', () => {
			userServiceSpy.postWishlistItem.and.returnValue(of(void 0));
			service.toggleWishlist(1, true);
			expect(authServiceSpy.getLoggedUser).toHaveBeenCalled();
			expect(userServiceSpy.postWishlistItem).toHaveBeenCalledWith('/user/1', 1);
		});

		it('should call deleteWishlistItem when add is false', () => {
			userServiceSpy.deleteWishlistItem.and.returnValue(of(void 0));
			service.toggleWishlist(1, false);
			expect(authServiceSpy.getLoggedUser).toHaveBeenCalled();
			expect(userServiceSpy.deleteWishlistItem).toHaveBeenCalledWith('/user/1', 1);
		});
	});
});
