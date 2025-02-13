import {AfterContentChecked, Component, OnInit, signal} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {NavbarComponent} from "./shared/components/navbar/navbar.component";
import {AuthService} from "./shared/services/auth.service";
import {concatMap, filter, Observable, of, switchMap} from "rxjs";
import {User} from "./shared/model/user/user";
import {AsyncPipe} from "@angular/common";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
  isLoggedIn = signal(true);
  user: Observable<User | null> = of(null);

  showSearchBar = true;

  constructor(
      private authService: AuthService,
      private router: Router,
      private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.user = this.authService.isLoggedIn$.pipe(
        concatMap(status => {
          this.isLoggedIn.set(status);
          return status? this.authService.getLoggedUser() : of(null);
        })
    );



    this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const currentRoute = this.router.url;

      const noSearchBar = [
          /^\/login$/,
          /^\/signup$/,
          /^\/search$/,
          /^\/validate$/,
          /^\/change-password$/,
          /^\/forgot-password$/,
          /^\/reset-password$/,
          /^\/add-book$/,
          /^\/book\/\d+\/edit$/,
          /^\/book\/\d+\/buy$/,
          /^\/book\/\d+\/deal$/,
          /^\/edit-profile$/
      ];

      this.showSearchBar = !noSearchBar.some(pattern => pattern.test(currentRoute));

    })
  }


  logout(){
    this.authService.logout();
  }



}
