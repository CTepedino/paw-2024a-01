import {Injectable} from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {concatMap, Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class ResetPasswordService {

  constructor(
      private authService: AuthService,
      private userService: UserService
  ) { }


  resetPassword(newPassword: string): Observable<void>{
    return this.authService.getLoggedUser().pipe(
        concatMap(user => this.userService.putPassword(user?.self!, newPassword))
    );
  }
}
