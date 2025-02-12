import {Component, OnInit} from '@angular/core';
import {RouterOutlet} from '@angular/router';
import {NavbarComponent} from "./shared/components/navbar/navbar.component";
import {AuthService} from "./shared/services/auth.service";
import {Observable, of, switchMap} from "rxjs";
import {User} from "./shared/model/user/user";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit{
  isLoggedIn = true;
  user: Observable<User | null> = of(null);

  constructor(
      private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.isLoggedIn$.pipe(
        switchMap(status => {
          this.isLoggedIn = status;
          return status? this.authService.getLoggedUser() : of(null);
        })
    );
  }


  logout(){
    this.authService.logout();
  }



}
