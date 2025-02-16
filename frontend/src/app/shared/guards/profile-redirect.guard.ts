import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {map} from "rxjs";

export const profileRedirectGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  return authService.getLoggedUserFromApi().pipe(
      map(user => {
        if (user != null){
          router.navigate([`/profile/${user?.id}`])
        } else {
          router.navigate([`/login`])
        }
        return false;
      })
  )
};
