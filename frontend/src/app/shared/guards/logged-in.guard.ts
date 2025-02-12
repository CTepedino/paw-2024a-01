import {CanActivateFn, Router} from '@angular/router';
import {inject} from "@angular/core";
import {AuthService} from "../services/auth.service";

export const loggedInGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.getJwtToken();
  if (token == null){
    router.navigate(['/login'], {queryParams: {redirect: state.url}});
    return false;
  }
  return true;
};
