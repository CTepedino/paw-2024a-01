import {Injectable} from '@angular/core';
import {BookWithDataService} from "../../../shared/services/book-with-data.service";
import {BookWithData} from "../../../shared/model/book/bookWithData";
import {concatMap, Observable, of, tap} from "rxjs";
import {OrderService} from "../../../shared/services/order.service";

@Injectable({
  providedIn: 'root'
})
export class BookDetailsService {

  constructor(private bookService: BookWithDataService, private orderService: OrderService) { }

  private book: BookWithData | undefined;

  getBook(id: string | number): Observable<BookWithData>{
    if (this.book?.id == id){
      return of(this.book!);
    }

    return this.bookService.getBookWithData(id).pipe(
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


}
