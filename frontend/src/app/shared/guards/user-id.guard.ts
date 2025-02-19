import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {UserService} from "../services/user.service";
import {catchError, forkJoin, map, of} from "rxjs";

export const userIdGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);

  const router = inject(Router);

  const id = route.parent?.paramMap.get('id') || route.paramMap.get('id');


  return authService.getLoggedUserFromApi().pipe(
      map(user => {
          if (user?.id != Number(id)){
              router.navigate([`/profile/${id}`]);
              return false;
          }
          return true;
      })
  );
};
