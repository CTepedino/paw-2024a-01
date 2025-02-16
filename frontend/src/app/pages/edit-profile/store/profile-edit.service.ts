import {Injectable} from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {concatMap, Observable, of, tap} from "rxjs";
import {User} from "../../../shared/model/user/user";
import {FormGroup} from "@angular/forms";

@Injectable({
  providedIn: 'root'
})
export class ProfileEditService {

  constructor(private authService: AuthService, private userService: UserService) { }

  getLoggedUser(): Observable<User | null | undefined> {
    return this.authService.getLoggedUser();
  }

  updateProfile(form: FormGroup): Observable<void>{
    return this.authService.getLoggedUser().pipe(
        concatMap(user => {
          let newUser = {
            firstName: form.get('firstName')?.value,
            lastName: form.get('lastName')?.value,
            description: form.get('description')?.value,
            cbu: undefined
          }
          if (form.get('cbu')?.value){
              newUser = {...newUser, cbu: form.get('cbu')?.value}
          }
          return this.userService.putUser(user?.self!, newUser).pipe(
              concatMap(() => {
                let toReturn: Observable<void> = of(void 0)
                if (form.get('pfp')?.get('fileData')?.value){
                    toReturn = this.userService.putProfilePicture(user?.self!, form.get('pfp')?.get('fileData')?.value)
                }
                return toReturn.pipe(
                    tap(() => {this.authService.resetLoggedUser()}));
              })
          )
        })
    );
  }
}
