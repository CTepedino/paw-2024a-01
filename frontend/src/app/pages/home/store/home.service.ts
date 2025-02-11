import {Injectable} from '@angular/core';
import {GenreService} from "../../../shared/services/genre.service";
import {BookService} from "../../../shared/services/book.service";
import {BookGenre} from "../../../shared/model/book/bookGenre";
import {forkJoin, map, Observable, of, switchMap} from "rxjs";
import {BookSearchOrderBy} from "../../../shared/model/book/bookSearchOrderBy";
import {UserService} from "../../../shared/services/user.service";
import {BookWithInfo} from "../../../shared/model/book/bookWithInfo";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {BookSearchQuery} from "../../../shared/model/book/bookSearchQuery";
import {User} from "../../../shared/model/user/user";
import {Deal} from "../../../shared/model/book/deal";

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(
      private genreService: GenreService,
      private bookService: BookService,
      private userService: UserService
  ){}

  getPopularGenres(size: number): Observable<BookGenre[]> {
    return this.genreService.getGenresByBookCount(1, size).pipe(
        map((r) => r.data)
    );
  }

  getBestSellers(size: number): Observable<BookWithInfo[]> {
    return this.listBooksWithData({size: size, order_by: BookSearchOrderBy.BEST_SELLERS}).pipe(
        map(bookPage => bookPage.data)
    );
  }

  getNewDeals(size: number): Observable<BookWithInfo[]> {
    return this.listBooksWithData({size: size, order_by: BookSearchOrderBy.NEW_DEALS}).pipe(
        map(bookPage => bookPage.data)
    );
  }

  getRecentBooks(page: number, size: number): Observable<PaginatedContent<BookWithInfo>> {
    return this.listBooksWithData({page: page, size: size, order_by: BookSearchOrderBy.PUBLICATION_DATE_DESC});
  }


  private writerCache: Map<string, User> = new Map<string, User>;
  private dealCache: Map<string, Deal> = new Map<string, Deal>;

  private listBooksWithData(query: BookSearchQuery = {}): Observable<PaginatedContent<BookWithInfo>> {
    return this.bookService.listBooks(query).pipe(
        switchMap((paginatedBooks) => {
          const bookRequests = paginatedBooks.data.map((book) => {

            const writer$ = this.fetchWriter(book.writer!);
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

  private fetchWriter(writerUrl: string): Observable<User>{
    if (this.writerCache.has(writerUrl)){
      return of(this.writerCache.get(writerUrl)!);
    }
    return this.userService.getUser(writerUrl).pipe(
      map((writer) => {
        this.writerCache.set(writerUrl, writer);
        return writer;
      })
    );
  }

  private fetchDeal(dealUrl: string | undefined): Observable<Deal | null>{
    if (dealUrl == null){
      return of(null);
    }
    if (this.dealCache.has(dealUrl)){
      return of(this.dealCache.get(dealUrl)!);
    }
    return this.bookService.getDeal(dealUrl).pipe(
        map((deal) => {
          this.writerCache.set(dealUrl, deal);
          return deal;
        })
    );
  }

}
