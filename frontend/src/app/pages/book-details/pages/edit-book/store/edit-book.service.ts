import {Injectable} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {concatMap, forkJoin, map, Observable, tap} from "rxjs";
import {Book} from "../../../../../shared/model/book/book";
import {BookService} from "../../../../../shared/services/book.service";
import {BookDetailsService} from "../../../store/book-details.service";

@Injectable({
  providedIn: 'root'
})
export class EditBookService {

  constructor(private bookService: BookService, private bookDetailsService: BookDetailsService) { }

    bookUrl: string = '';

  getBook(id: any): Observable<Book>{
    return this.bookDetailsService.getBook(id).pipe(
        tap(book => this.bookUrl = book.self!)
    );
  }

  edit(bookEditForm: FormGroup): Observable<void> {
      const actions = [
          this.bookService.putBook(this.bookUrl, {
              title: bookEditForm.get('title')?.value,
              description: bookEditForm.get('description')?.value,
              genre: bookEditForm.get('genre')?.value,
              suggestedAge: bookEditForm.get('suggestedAge')?.value,
              price: bookEditForm.get('price')?.value,
              pageCount: bookEditForm.get('pageCount')?.value,
          })
      ]

      if (bookEditForm.get('cover')?.get('fileData')?.value){
          actions.push(this.bookService.putBookCover(this.bookUrl, bookEditForm.get('cover')?.get('fileData')?.value));
      }
      if (bookEditForm.get('preview')?.get('fileData')?.value){
          actions.push(this.bookService.putBookCover(this.bookUrl, bookEditForm.get('preview')?.get('fileData')?.value));
      }
      if (bookEditForm.get('file')?.get('fileData')?.value){
          actions.push(this.bookService.putBookCover(this.bookUrl, bookEditForm.get('file')?.get('fileData')?.value));
      }

      return forkJoin(actions).pipe(
          concatMap(() => this.bookDetailsService.reloadBook())
      );
  }

}
