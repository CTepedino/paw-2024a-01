import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../services/user.service";
import {catchError, map, of} from "rxjs";

export const userExistsGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router)

  const id = route.params['id'];

  return userService.getUserById(id).pipe(
      map(() => true),
      catchError(() => {
        router.navigate(['/404'])
        return of(false)
      })
  )
};
