import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {BookService} from "../services/book.service";
import {catchError, forkJoin, map, of} from "rxjs";

export const isNotBookWriterGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const bookService = inject(BookService);
  const router = inject(Router);

  const id = route.paramMap.get('id') || route.parent?.paramMap.get('id');

  return forkJoin({
    loggedUser: authService.getLoggedUserFromApi(),
    book: bookService.getBookById(Number(id))
  }).pipe(
      map(({loggedUser, book}) => {
        if (loggedUser == null || loggedUser.self == book.writer){
          router.navigate(['/']);
          return false;
        }
        return true;
      }),
      catchError(() => {
        router.navigate(['/']);
        return of(false);
      })
  )
};
