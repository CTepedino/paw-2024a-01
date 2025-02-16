import { Injectable } from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {User} from "../../../shared/model/user/user";
import {catchError, concatMap, map, Observable, of, tap} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  constructor(private authService: AuthService, private userService: UserService) { }

  private user: User | null = null;
  private isOwner: boolean = false;

  getUser(id: any): Observable<ProfileDisplayInfo>{
    return this.userService.getUserById(id).pipe(
        concatMap((user) => {
          return this.authService.getLoggedUser().pipe(
              map(loggedUser => new ProfileDisplayInfo(user, loggedUser != null && user.id == loggedUser.id)),
              catchError(() => of(new ProfileDisplayInfo(user, false)))
          )
        })
    );
  }
}

export class ProfileDisplayInfo {
  user: User;
  isOwner: boolean;

  constructor(user: User, isOwner: boolean) {
    this.user = user;
    this.isOwner = isOwner;
  }
}