import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {UserService} from "../services/user.service";
import {catchError, map, of} from "rxjs";
import {UserRoles} from "../model/user/userRoles";

export const idIsWriterGuard: CanActivateFn = (route, state) => {
  const userService = inject(UserService);
  const router = inject(Router);

  const id = route.parent?.paramMap.get('id');

  return userService.getUserById(Number(id)).pipe(
      map(user => {
        if (!user.roles?.includes(UserRoles.WRITER)){
          router.navigate([`/profile/${id}`]);
          return false;
        }
        return true;
      }),
      catchError(err => {
        router.navigate([`/profile/${id}`]);
        return of(false);
      })
  );
};
