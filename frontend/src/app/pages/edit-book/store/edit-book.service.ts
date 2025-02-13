import {Injectable} from '@angular/core';
import {BookService} from "../../../shared/services/book.service";
import {FormGroup} from "@angular/forms";
import {forkJoin, map, Observable, tap} from "rxjs";
import {Book} from "../../../shared/model/book/book";

@Injectable({
  providedIn: 'root'
})
export class EditBookService {

  constructor(private bookService: BookService) { }

  private bookUrl: string = '';

  getBook(id: any): Observable<Book>{
    return this.bookService.getBookById(id).pipe(
        tap(book => {
          this.bookUrl = book.self!;
        })
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

      return forkJoin(actions).pipe(map(() => {return;}))
  }

}
