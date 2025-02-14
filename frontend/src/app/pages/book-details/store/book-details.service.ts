import {Injectable} from '@angular/core';
import {BookWithDataService} from "../../../shared/services/book-with-data.service";
import {BookWithData} from "../../../shared/model/book/bookWithData";
import {catchError, concatMap, forkJoin, map, Observable, of, tap} from "rxjs";
import {OrderService} from "../../../shared/services/order.service";
import {FormGroup} from "@angular/forms";
import {BookService} from "../../../shared/services/book.service";
import {AuthService} from "../../../shared/services/auth.service";
import {Review} from "../../../shared/model/review/review";

@Injectable({
  providedIn: 'root'
})
export class BookDetailsService {

  constructor(private bookWithDataService: BookWithDataService, private orderService: OrderService, private bookService: BookService, private authService: AuthService) { }

  private book: BookWithData | undefined;

  getBook(id: string | number): Observable<BookWithData>{
    if (this.book?.id == id){
      return of(this.book!);
    }

    return this.bookWithDataService.getBookWithData(id).pipe(
        tap((book) => this.book = book)
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
          return this.bookService.putDeal(
              book.self!,
              {
                price: form.get('price')?.value,
                end: form.get('endDate')?.value.toISOString().substring(0, 10)
              }
          );
        })
    )
  }

  endDeal(id: number): Observable<void> {
    return this.getBook(id).pipe(
        concatMap(book => this.bookService.deleteDeal(book.deal!))
    );
  }

  getLoggedReview(bookId: any): Observable<Review | null>{
      return forkJoin({
          user$: this.authService.getLoggedUser(),
          book$: this.getBook(bookId)
      }).pipe(
          concatMap(response => this.bookService.getReview(response.book$.self!, response.user$?.id!))
      );
  }

  setReview(bookId: any, form: FormGroup): Observable<void>{
      return forkJoin({
          user$: this.authService.getLoggedUser(),
          book$: this.bookService.getBookById(bookId)
      }).pipe(
          concatMap(response => {
              return this.bookService.putReview(response.book$.self!, response.user$?.id!,{
                  rating: form.get('rating')?.value * 2,
                  review: form.get('review')?.value
              })
          })
      );
  }

}
