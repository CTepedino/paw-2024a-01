import {HttpErrorResponse, HttpInterceptorFn, HttpResponse} from '@angular/common/http';
import {inject} from "@angular/core";
import {ApiAuthService} from "../services/api-auth.service";
import {catchError, switchMap, tap, throwError} from "rxjs";


export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(ApiAuthService)
  let modifiedReq = req;

  const jwtToken = authService.getJwtToken();
  if (jwtToken){
    modifiedReq = req.clone({
      setHeaders: {
        Authorization: jwtToken
      }
    });
  }

  return next(modifiedReq).pipe(
      tap(event => {
        if (event instanceof HttpResponse) {
          const newJwt = event.headers.get('Authorization');
          const newRefreshToken = event.headers.get('X-Refresh-Token');

          if (newJwt && newRefreshToken) {
            authService.storeTokens(newJwt, newRefreshToken);
          }
        }
      }),
      catchError((error: HttpErrorResponse) => {
        if (error.status === 401) {
          return authService.refreshToken().pipe(
              switchMap(() => {
                const newToken = authService.getJwtToken();
                if (!newToken) {
                  return throwError(() => error);
                }

                const retryReq = req.clone({
                  setHeaders: {
                    Authorization: newToken
                  }
                });

                return next(retryReq);
              }),
              catchError(refreshError => {
                authService.logout();
                return throwError(() => refreshError);
              })
          );
        }

        return throwError(() => error);
      })
  );
};
