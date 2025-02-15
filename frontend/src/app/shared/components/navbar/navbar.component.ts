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


  logout = output<void>();

  constructor(private translationService: TranslationService) {
  }

  getTranslation(key: string): string {
    return this.translationService.getTranslation(key);
  }

  protected readonly UserRoles = UserRoles;
}
