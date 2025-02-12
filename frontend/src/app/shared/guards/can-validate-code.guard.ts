import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";
import {catchError, map, of} from "rxjs";

export const canValidateCodeGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router)

  if (authService.getJwtToken()){
    router.navigate(['/']);
    return false;
  }

  const id =  route.queryParams['id'];
  const code = route.queryParams['code'];
  if (!id || !code){
    router.navigate(['/']);
    return false;
  }


  return authService.validateCode(id, code).pipe(
      map(() => {
        if (!authService.getJwtToken()){
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
