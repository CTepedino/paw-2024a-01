import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {catchError, map, of, throwError} from "rxjs";
import {UserRoles} from "../model/user/userRoles";

export const writerGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getLoggedUserFromApi().pipe(
      map(user => {
        if (!user || !user.roles?.includes(UserRoles.WRITER)){
          router.navigate(['/']);
          return false;
        }
        return true;
      }),
      catchError(err => {
        router.navigate(['/']);
        return of(false);
      })
  )
};
