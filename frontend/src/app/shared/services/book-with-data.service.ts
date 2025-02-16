import { Injectable } from '@angular/core';
import {BookSearchQuery} from "../model/book/bookSearchQuery";
import {catchError, concatMap, forkJoin, map, Observable, of, switchMap} from "rxjs";
import {PaginatedContent} from "../model/paginatedContent";
import {BookWithData} from "../model/book/bookWithData";
import {User} from "../model/user/user";
import {Deal} from "../model/book/deal";
import {BookService} from "./book.service";
import {UserService} from "./user.service";
import {Book} from "../model/book/book";

@Injectable({
  providedIn: 'root'
})
export class BookWithDataService {

    constructor(private bookService: BookService, private userService: UserService) { }

    writers: Map<string, User> = new Map<string, User>;

    listBooksWithData(query: BookSearchQuery = {}): Observable<PaginatedContent<BookWithData>> {
    return this.bookService.listBooks(query).pipe(
        concatMap((paginatedBooks) => {
          const bookRequests = paginatedBooks.data.map((book) => this.fillBook(book));
          return forkJoin(bookRequests).pipe(
              map((booksWithData) =>({
                data: booksWithData,
                pagination: paginatedBooks.pagination
              }))
          );

        })
    )
    }

    getBookWithData(id: string | number): Observable<BookWithData>{
        return this.bookService.getBookById(id).pipe(
            concatMap(book => this.fillBook(book))
        )
    }


    private fillBook(book: Book): Observable<BookWithData> {
      const writer$ = this.fetchWriter(book.writer!);
      const deal$ = this.fetchDeal(book.deal);

      return forkJoin({writerInfo: writer$, dealInfo: deal$}).pipe(
          map(({writerInfo, dealInfo}) => ({
              ...book,
              writerInfo: writerInfo,
              dealInfo: dealInfo
          }))
      );
    }

    private fetchWriter(writerUrl: string): Observable<User>{
        if (this.writers.has(writerUrl)){
          return of(this.writers.get(writerUrl)!);
        }
        return this.userService.getUser(writerUrl).pipe(
            map((writer) => {
                this.writers.set(writerUrl, writer);
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

