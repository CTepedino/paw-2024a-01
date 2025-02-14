import {Injectable} from '@angular/core';
import {BookWithDataService} from "../../../shared/services/book-with-data.service";
import {BookWithData} from "../../../shared/model/book/bookWithData";
import {concatMap, Observable, of, tap} from "rxjs";
import {OrderService} from "../../../shared/services/order.service";
import {FormGroup} from "@angular/forms";
import {BookService} from "../../../shared/services/book.service";

@Injectable({
  providedIn: 'root'
})
export class BookDetailsService {

  constructor(private bookWithDataService: BookWithDataService, private orderService: OrderService, private bookService: BookService) { }

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
}
