import {Injectable} from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {BookService} from "../../../shared/services/book.service";
import {UserService} from "../../../shared/services/user.service";
import {concatMap, forkJoin, map, Observable, of, switchMap} from "rxjs";
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class AddBookService {

  constructor(private authService: AuthService, private bookService: BookService, private userService: UserService) { }

  shouldShowCBUField(): Observable<boolean>{
    return this.authService.getLoggedUser().pipe(
        map(user => user?.cbu == null)
    );
  }

  publish(bookPublishForm: FormGroup): Observable<number> {
    return this.authService.getLoggedUser().pipe(
        concatMap(user => {
          if (user?.cbu == null){
            return this.userService.putUser(user?.self!, {
              firstName: user?.firstName,
              lastName: user?.lastName,
              cbu: bookPublishForm.get('cbu')?.value,
              description: user?.description
            }).pipe(concatMap(() => this.postBook(bookPublishForm)))
          } else {
            return this.postBook(bookPublishForm);
          }
        })
    );
  }

  private postBook(bookPublishForm: FormGroup): Observable<number>{
    return this.bookService.postBook({
      title: bookPublishForm.get('title')?.value,
      description: bookPublishForm.get('description')?.value,
      genre: bookPublishForm.get('genre')?.value,
      suggestedAge: bookPublishForm.get('suggestedAge')?.value,
      price: bookPublishForm.get('price')?.value,
      pageCount: bookPublishForm.get('pageCount')?.value,
      publicationDate: bookPublishForm.get('publicationDate')?.value.toISOString().substring(0, 10)
    }).pipe(
        switchMap(bookUrl => {
          return forkJoin([
            this.bookService.putBookCover(bookUrl, bookPublishForm.get('cover')?.get('fileData')?.value),
            this.bookService.putBookPreview(bookUrl, bookPublishForm.get('preview')?.get('fileData')?.value),
            this.bookService.putBookFile(bookUrl, bookPublishForm.get('file')?.get('fileData')?.value)
          ]).pipe(
              concatMap(()=> {
                return this.bookService.getBook(bookUrl).pipe(
                  map((book) => book?.id ?? 0)
                );
          }))
        })
    )
  }


}
