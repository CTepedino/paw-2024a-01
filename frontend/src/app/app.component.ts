import {AfterContentChecked, Component, computed, inject, OnInit, signal} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {NavbarComponent} from "./shared/components/navbar/navbar.component";
import {AuthService} from "./shared/services/auth.service";
import {concatMap, filter, Observable, of, switchMap} from "rxjs";
import {User} from "./shared/model/user/user";
import {AsyncPipe} from "@angular/common";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, NavbarComponent, AsyncPipe, TranslateModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent implements OnInit {
    user = signal<User | null | undefined>(undefined);
    isLoggedIn = signal<boolean | undefined>(undefined);
    pfp = signal<string | null>(null);

    showSearchBar = true;


  constructor(
      private authService: AuthService,
      private router: Router,
      private route: ActivatedRoute,
      private translate: TranslateService
  ) {
      this.initializeTranslation();
  }

  ngOnInit(): void {
    this.authService.getLoggedUser().subscribe(user => {
        this.user.set(user);
        if (user !== undefined){
            this.isLoggedIn.set(user != null);
        }
        if (user){
            this.pfp.set(`${this.user()?.profilePicture}?height=50&width=50&t=${new Date().getTime()}`)
        }
    })



    this.router.events.pipe(
        filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const currentRoute = this.router.url;

      const noSearchBar = [
          /^\/login.*$/,
          /^\/signup.*$/,
          /^\/search.*$/,
          /^\/validate.*$/,
          /^\/change-password.*$/,
          /^\/forgot-password.*$/,
          /^\/reset-password.*$/,
          /^\/add-book.*$/,
          /^\/book\/\d+\/edit.*$/,
          /^\/book\/\d+\/buy.*$/,
          /^\/book\/\d+\/deal.*$/,
          /^\/edit-profile.*$/
      ];

      this.showSearchBar = !noSearchBar.some(pattern => pattern.test(currentRoute));

    })
  }

    initializeTranslation() {
        // Detectar idioma del navegador
        const browserLang = navigator.language.split('-')[0]; // Ejemplo: "es-ES" → "es"
        const defaultLang = 'en'; // Idioma por defecto

        // Lista de idiomas soportados
        const availableLangs = ['en', 'es'];

        // Usar el idioma del navegador si está disponible, sino usar el idioma por defecto
        const selectedLang = availableLangs.includes(browserLang) ? browserLang : defaultLang;

        this.translate.setDefaultLang(defaultLang);
        this.translate.use(selectedLang);
    }


  logout(){
    this.authService.logout();
  }



}
