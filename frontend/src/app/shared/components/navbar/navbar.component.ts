import { Component, input, output, signal } from '@angular/core';
import { MatToolbar, MatToolbarRow } from '@angular/material/toolbar';
import { NgOptimizedImage } from '@angular/common';
import { RouterLink } from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatFormField, MatPrefix } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { NavbarMenuItemComponent } from '../navbar-menu-item/navbar-menu-item.component';
import { NavbarMenuComponent } from '../navbar-menu/navbar-menu.component';
import { UserRoles } from '../../model/user/userRoles';
import { NavButtonComponent } from '../nav-button/nav-button.component';
import { User } from '../../model/user/user';
import {TranslationService} from "../../services/translation.service";


@Component({
  selector: 'app-navbar',
  imports: [
    MatToolbar,
    MatToolbarRow,
    NgOptimizedImage,
    RouterLink,
    MatIcon,
    MatFormField,
    MatPrefix,
    MatInput,
    NavButtonComponent,
    NavbarMenuItemComponent,
    NavbarMenuComponent,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  showSearchBar = input(true);
  showRightBar = input(true);

  isLoggedIn = input<boolean | undefined>(undefined);
  loggedUser = input<User | null | undefined>(undefined);
  pfp = input<string>('');

  // Definir la variable de traducción
  currentLang = 'en';
  translations = signal<{ [key: string]: string }>({});

  logout = output<void>();

  constructor(private translationService: TranslationService) {
    // Cargar las traducciones al inicio
    const savedLang = localStorage.getItem('lang') || 'en';
    this.changeLanguage(savedLang);
  }

  changeLanguage(lang: string) {
    this.translationService.setLanguage(lang);
    this.currentLang = lang;

    this.translationService.loadTranslations(lang).subscribe((data) => {
      this.translations.set(data);
      localStorage.setItem('lang', lang);  // Guardar el idioma en localStorage
    });
  }

  protected readonly UserRoles = UserRoles;
}
