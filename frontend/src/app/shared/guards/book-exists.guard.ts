import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {catchError, map, of} from "rxjs";
import {BookService} from "../services/book.service";

export const bookExistsGuard: CanActivateFn = (route, state) => {
  const bookService = inject(BookService);
  const router = inject(Router)

  const id = route.params['id'];

  return bookService.getBookById(id).pipe(
      map(() => true),
      catchError(() => {
        router.navigate(['/404'])
        return of(false)
      })
  )
};
