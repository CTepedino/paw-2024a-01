import { Injectable } from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {catchError, concatMap, map, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ChangePasswordService {

  constructor(private authService : AuthService, private userService: UserService) { }

  validateOld(oldPassword: string): Observable<boolean>{
    return this.authService.getLoggedUser().pipe(
        concatMap(user => this.authService.verifyCredentials(user?.email ?? '', oldPassword))
    );
  }

  resetPassword(password: string): Observable<void> {
    return this.authService.getLoggedUser().pipe(
        concatMap(user => this.userService.putPassword(user?.self ?? '', password))
    );
  }

}
