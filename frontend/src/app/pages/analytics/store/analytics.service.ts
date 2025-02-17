import { Injectable } from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {BookService} from "../../../shared/services/book.service";
import {concatMap, forkJoin, map, Observable} from "rxjs";
import {Book} from "../../../shared/model/book/book";
import {PaginatedContent} from "../../../shared/model/paginatedContent";

@Injectable({
  providedIn: 'root'
})
export class AnalyticsService {

  constructor(private authService: AuthService, private userService: UserService, private bookService: BookService) { }

  getTotal(): Observable<Analytics>{
    return this.authService.getLoggedUser().pipe(
        map(user => {
          return new Analytics(user?.orderCount!, user?.salesTotal!);
        })
    )
  }

  getCurrentMonthTotal(): Observable<Analytics> {
    return this.authService.getLoggedUser().pipe(
        concatMap(user => {
          return this.userService.getWriterMonthlyAnalytics(user?.currentMonthlyAnalytics!).pipe(
              map(analytics => new Analytics(analytics?.orderCount!, analytics?.salesTotal!))
          )
        })
    )
  }

  getMonthlyTotal(year: number, month: number): Observable<Analytics> {
    return this.authService.getLoggedUser().pipe(
        concatMap(user => {
          return this.userService.getWriterMonthlyAnalyticsFromWriter(user?.self!, `${year}-${month < 10? '0':''}${month}`).pipe(
              map(analytics => new Analytics(analytics?.orderCount!, analytics?.salesTotal!))
          )
        })
    )
  }

  getBooksWithAnalytics(page: number, size: number): Observable<PaginatedContent<BookWithAnalytics>> {
    return this.authService.getLoggedUser().pipe(
        concatMap(user => this.bookService.listBooks({writer_id: user?.id, page: page, size: size}).pipe(
              map(books => {
                const booksWithAnalytics = books.data.map(b => new BookWithAnalytics(b, new Analytics(b.orderCount!, b.salesTotal!)))

                return {pagination: books.pagination, data: booksWithAnalytics}
              })
            )
        )
    );
  }

  getBooksWithMonthlyAnalytics(year: number, month: number, page: number, size: number){
    return this.authService.getLoggedUser().pipe(
        concatMap(user => this.bookService.listBooks({writer_id: user?.id, page: page, size: size}).pipe(
                concatMap(books => {
                  const booksWithAnalytics$ = books.data.map( b => {
                    return this.bookService.getBookMonthlyAnalyticsFromBook(b.self!, `${year}-${month < 10? '0':''}${month}`).pipe(
                        map(analytics => new BookWithAnalytics(b, new Analytics(analytics.orderCount!, analytics.salesTotal!)))
                    )
                  })

                  return forkJoin(booksWithAnalytics$).pipe(
                      map(booksWithAnalytics => ({pagination: books.pagination, data: booksWithAnalytics}))
                  )
                })
            )
        )
    );
  }

}


export class Analytics {
  orderCount: number;
  salesTotal: number;

  constructor(orderCount: number, salesTotal: number) {
    this.orderCount = orderCount;
    this.salesTotal = salesTotal;
  }

}

export class BookWithAnalytics {
  book: Book;
  analytics: Analytics;


  constructor(book: Book, analytics: Analytics) {
    this.book = book;
    this.analytics = analytics;
  }
}