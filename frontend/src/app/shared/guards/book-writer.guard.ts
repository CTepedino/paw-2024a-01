import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {UserService} from "../services/user.service";
import {catchError, forkJoin, map, of} from "rxjs";
import {BookService} from "../services/book.service";

export const bookWriterGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const bookService = inject(BookService);
  const router = inject(Router);

  const id = route.paramMap.get('id');

  return forkJoin({
    loggedUser: authService.getLoggedUserFromApi(),
    book: bookService.getBookById(Number(id))
  }).pipe(
      map(({loggedUser, book}) => {
        if (loggedUser == null || loggedUser.self != book.writer){
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
