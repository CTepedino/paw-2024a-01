import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {UserService} from "../services/user.service";
import {catchError, forkJoin, map, of} from "rxjs";

export const userIdGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const userService = inject(UserService);
  const router = inject(Router);

  const id = route.paramMap.get('id');

  return forkJoin({
    loggedUser: authService.getLoggedUser(),
    user: userService.getUserById(Number(id))
  }).pipe(
      map(({loggedUser, user}) => {
        if (loggedUser == null || loggedUser.id != user.id){
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
