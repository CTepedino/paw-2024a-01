import {AfterContentChecked, Component, OnInit, signal} from '@angular/core';
import {ActivatedRoute, NavigationEnd, Router, RouterOutlet} from '@angular/router';
import {NavbarComponent} from "./shared/components/navbar/navbar.component";
import {AuthService} from "./shared/services/auth.service";
import {concatMap, filter, Observable, of, switchMap} from "rxjs";
import {User} from "./shared/model/user/user";
import {AsyncPipe} from "@angular/common";
import { TranslationService } from './shared/services/translation.service';

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

    currentLang: string = 'en';  // Default language
    translations: { [key: string]: string } = {};

  constructor(
      private authService: AuthService,
      private router: Router,
      private route: ActivatedRoute,
      private translationService: TranslationService
  ) {}

  ngOnInit(): void {
    this.user = this.authService.isLoggedIn$.pipe(
        concatMap(status => {
          this.isLoggedIn.set(status);
          return status? this.authService.getLoggedUser() : of(null);
        })
    );

      const savedLang = localStorage.getItem('lang') || navigator.language.split('-')[0] || 'en';

      // Configurar el idioma al inicializar
      const browserLang = navigator.language.split('-')[0];  // Extract language code (e.g., 'en' from 'en-US')

      // List of supported languages
      const supportedLanguages = ['en', 'es'];

      // If the browser's language is not supported, fall back to English
      const defaultLang = supportedLanguages.includes(browserLang) ? browserLang : 'en';

      // Set the language
      this.changeLanguage(defaultLang);



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

    changeLanguage(lang: string): void {
        this.translationService.setLanguage(lang);
        this.currentLang = lang;

        this.translationService.loadTranslations(lang).subscribe((data) => {
            this.translations = data;
        });
    }


  logout(){
    this.authService.logout();
  }



}
