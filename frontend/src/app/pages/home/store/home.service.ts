import {Injectable} from '@angular/core';
import {GenreService} from "../../../shared/services/genre.service";
import {BookGenre} from "../../../shared/model/book/bookGenre";
import {map, Observable} from "rxjs";
import {BookSearchOrderBy} from "../../../shared/model/book/bookSearchOrderBy";
import {BookWithInfo} from "../../../shared/model/book/bookWithInfo";
import {PaginatedContent} from "../../../shared/model/paginatedContent";
import {BookWithDataService} from "../../../shared/services/book-with-data.service";

@Injectable({
  providedIn: 'root'
})
export class HomeService {

  constructor(
      private genreService: GenreService,
      private bookService: BookWithDataService
  ){}

  getPopularGenres(size: number): Observable<BookGenre[]> {
    return this.genreService.getGenresByBookCount(1, size).pipe(
        map((r) => r.data)
    );
  }

  getBestSellers(size: number): Observable<BookWithInfo[]> {
    return this.bookService.listBooksWithData({size: size, order_by: BookSearchOrderBy.BEST_SELLERS}).pipe(
        map(bookPage => bookPage.data)
    );
  }

  getNewDeals(size: number): Observable<BookWithInfo[]> {
    return this.bookService.listBooksWithData({size: size, order_by: BookSearchOrderBy.NEW_DEALS}).pipe(
        map(bookPage => bookPage.data)
    );
  }

  getRecentBooks(page: number, size: number): Observable<PaginatedContent<BookWithInfo>> {
    return this.bookService.listBooksWithData({page: page, size: size, order_by: BookSearchOrderBy.PUBLICATION_DATE_DESC});
  }

}
