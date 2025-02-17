import {Injectable} from '@angular/core';
import {BookWithDataService} from "../../../shared/services/book-with-data.service";
import {BookWithData} from "../../../shared/model/book/bookWithData";
import {catchError, concatMap, forkJoin, map, Observable, of, tap} from "rxjs";
import {OrderService} from "../../../shared/services/order.service";
import {FormGroup} from "@angular/forms";
import {BookService} from "../../../shared/services/book.service";
import {AuthService} from "../../../shared/services/auth.service";
import {Review} from "../../../shared/model/review/review";
import {UserService} from "../../../shared/services/user.service";
import {OrderStatus} from "../../../shared/model/order/orderStatus";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {ReviewWithInfo} from "../../../shared/model/review/reviewWithInfo";
import {ReviewOrderBy} from "../../../shared/model/review/reviewOrderBy";
import {ReviewSearchQuery} from "../../../shared/model/review/reviewSearchQuery";
import {QuestionService} from "../../../shared/services/question.service";
import {Question} from "../../../shared/model/question/question";

@Injectable({
  providedIn: 'root'
})
export class BookDetailsService {

    constructor(
        private bookWithDataService: BookWithDataService,
        private orderService: OrderService,
        private bookService: BookService,
        private authService: AuthService,
        private userService: UserService,
        private questionService: QuestionService
    ) { }

    private book: BookWithData | undefined;

    getBook(id: number): Observable<BookWithData>{
        if (this.book?.id == id){
            return of(this.book!);
        }

        return this.bookWithDataService.getBookWithData(id).pipe(
            tap((book) => this.book = book)
        );
    }

    reloadBook(): Observable<void>{
        if (this.book){
            return this.bookWithDataService.getBookWithData(this.book.id!).pipe(
                tap((book) => this.book = book),
                map(() => void 0)
            );
        }
        return of(void 0);
    }

    isLoggedIn(): Observable<boolean> {
        return this.authService.getLoggedUser().pipe(
            map(user => !!user)
        );
    }

    isAuthor(bookId: number): Observable<boolean>{
        return this.getBook(bookId).pipe(
            concatMap(book => this.authService.getLoggedUser().pipe(
                map(user => book.writerInfo.id == user?.id)
            ))
        )
    }

    existsOrder(bookId: number): Observable<boolean> {
        return this.authService.getLoggedUser().pipe(
            concatMap(user => this.orderService.listOrders({book_id: bookId, buyer_id: user?.id!}).pipe(
                map(orders => orders.pagination.totalCount > 0)
            ))
        );
    }

    isOwner(bookId: number): Observable<boolean> {
        return this.authService.getLoggedUser().pipe(
            concatMap(user => this.orderService.listOrders({book_id: bookId, buyer_id: user?.id!, status: OrderStatus.COMPLETED}).pipe(
                map(orders => orders.pagination.totalCount > 0)
            ))
        );
    }

    buy(bookId: number, receipt: File): Observable<void>{
        return this.orderService.postOrder({
            bookId: bookId
        }).pipe(
            concatMap(orderUrl => this.orderService.putReceipt(orderUrl, receipt))
        );
    }

    setDeal(id: number, form: FormGroup): Observable<void> {
        return this.getBook(id).pipe(
            concatMap(book => {
                const deal = {
                    price: form.get('price')?.value,
                    end: form.get('endDate')?.value.toISOString().substring(0, 10)
                }
                this.book!.deal = book.self + '/deal';
                this.book!.dealInfo = deal;
                return this.bookService.putDeal(book.self!, deal);
            })
        )
    }

    endDeal(id: number): Observable<void> {
        return this.getBook(id).pipe(
            concatMap(book => {
                const toReturn = this.bookService.deleteDeal(book.deal!);
                this.book!.deal = undefined;
                this.book!.dealInfo = undefined;
                return toReturn;
            })
        );
    }

    getReviews(bookId: number, query: ReviewSearchQuery): Observable<PaginatedContent<ReviewWithInfo>> {
        return this.getBook(bookId).pipe(
            concatMap(book => this.bookService.listReviews(book?.self!, query).pipe(
                concatMap(reviews => {
                    if (reviews.data.length === 0){
                        return of({data: [], pagination: reviews.pagination});
                    }
                    const reviewers$ = reviews.data.map(r => this.userService.getUser(r.reviewer!).pipe(
                        map(reviewer => ({...r, reviewerInfo: reviewer}))
                    ));

                    return forkJoin(reviewers$).pipe(
                        map(reviewers => ({
                            data: reviewers,
                            pagination: reviews.pagination
                        }))
                    )
                })
            ))
        )
    }

    getLoggedReview(bookId: number): Observable<Review | null>{
        return this.authService.getLoggedUser().pipe(
            concatMap(user => {
                return this.bookService.getReview(bookId, user?.id!);
            }),
            catchError(() => of(null))
        );
    }

    setReview(bookId: number, form: FormGroup): Observable<void>{
        return this.getBook(bookId).pipe(
            concatMap(book => this.authService.getLoggedUser().pipe(
                concatMap(user => this.bookService.putReview(
                    book.self!,
                    user?.id!,
                    {
                        rating: form.get('rating')?.value,
                        review: form.get('review')?.value
                    }))
            ))
        );
    }

    isWishlisted(bookId: number): Observable<boolean> {
        return this.authService.getLoggedUser().pipe(
            concatMap(user => {
                if (!user) return of(false);
                return this.userService.getWishlistItem(user.self!, bookId).pipe(
                    map(() => true),
                    catchError(() => of(false))
                );
            })
        )
    }

    toggleWishlist(bookId: number, add: boolean) {
        this.authService.getLoggedUser().pipe(
            concatMap(user => {
                if (add){
                    return this.userService.postWishlistItem(user?.self!, bookId);
                } else {
                    return this.userService.deleteWishlistItem(user?.self!, bookId);
                }
          })
      ).subscribe();
    }

    isRecommended(bookId: number): Observable<boolean> {
        return this.authService.getLoggedUser().pipe(
            concatMap(user => {
                if (!user) return of(false);
                return this.userService.getRecommendation(user.self!, bookId).pipe(
                    map(() => true),
                    catchError(() => of(false))
                )
            })
        )
    }

    toggleRecommend(bookId: number, add: boolean) {
        this.authService.getLoggedUser().pipe(
            concatMap(user => {
                if (add){
                    return this.userService.postRecommendation(user?.self!, bookId);
                } else {
                    return this.userService.deleteRecommendation(user?.self!, bookId);
                }
            })
        ).subscribe();
    }

    getRecommendedBooks(bookId: number, size: number): Observable<BookWithData[]>{
        return this.bookWithDataService.listBooksWithData({recommendations_for_book: bookId, size: size}).pipe(
            map(page => page.data)
        );
    }

    getAllQuestions(bookId: number, page: number, size: number): Observable<PaginatedContent<Question>> {
        return this.questionService.listQuestions({book_id: bookId, page: page, size: size});
    }

    getAllMyQuestions(bookId: number, page: number, size: number): Observable<PaginatedContent<Question>> {
        return this.authService.getLoggedUser().pipe(
            concatMap(user => this.questionService.listQuestions({book_id: bookId, page: page, size: size, questioner_id: user?.id!}))
        );
    }

    getAllOtherQuestions(bookId: number, page: number, size: number): Observable<PaginatedContent<Question>> {
        return this.authService.getLoggedUser().pipe(
            concatMap(user => this.questionService.listQuestions({book_id: bookId, page: page, size: size, questioner_id: user?.id!, exclude_questioner: true}))
        );
    }

    askQuestion(bookId: number, question: string){
        return this.questionService.postQuestion({
            bookId: bookId,
            question: question,
        });
    }

    answerQuestion(questionUrl: string, answer: string){
        return this.questionService.putAnswer(questionUrl, {answer: answer, answerDate: new Date().toString()})
    }

    openPdf(url: string){
        return this.bookService.openBookInternal(url);
    }

}
