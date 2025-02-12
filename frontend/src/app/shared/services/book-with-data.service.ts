import { Injectable } from '@angular/core';
import {BookSearchQuery} from "../model/book/bookSearchQuery";
import {forkJoin, map, Observable, of, switchMap} from "rxjs";
import {PaginatedContent} from "../model/paginatedContent";
import {BookWithInfo} from "../model/book/bookWithInfo";
import {User} from "../model/user/user";
import {Deal} from "../model/book/deal";
import {BookService} from "./book.service";
import {UserService} from "./user.service";

@Injectable({
  providedIn: 'root'
})
export class BookWithDataService {

  constructor(private bookService: BookService, private userService: UserService) { }

  listBooksWithData(query: BookSearchQuery = {}): Observable<PaginatedContent<BookWithInfo>> {

    const writerCache: Map<string, User> = new Map<string, User>;

    return this.bookService.listBooks(query).pipe(
        switchMap((paginatedBooks) => {
          const bookRequests = paginatedBooks.data.map((book) => {



            const writer$ = this.fetchWriter(book.writer!, writerCache);
            const deal$ = this.fetchDeal(book.deal);

            return forkJoin({writerInfo: writer$, dealInfo: deal$}).pipe(
                map(({writerInfo, dealInfo}) => ({
                  ...book,
                  writerInfo: writerInfo,
                  dealInfo: dealInfo
                }))
            );
          });

          return forkJoin(bookRequests).pipe(
              map((booksWithData) => ({
                data: booksWithData,
                pagination: paginatedBooks.pagination
              }))
          );

        })
    )
  }

  private fetchWriter(writerUrl: string, writerCache: Map<string, User>): Observable<User>{
    if (writerCache.has(writerUrl)){
      return of(writerCache.get(writerUrl)!);
    }
    return this.userService.getUser(writerUrl).pipe(
        map((writer) => {
          writerCache.set(writerUrl, writer);
          return writer;
        })
    );
  }

  private fetchDeal(dealUrl: string | undefined): Observable<Deal | null>{
    if (dealUrl == null){
      return of(null);
    }

    return this.bookService.getDeal(dealUrl);
  }
}
