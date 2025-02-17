import {Component, inject, input, output, signal} from '@angular/core';
import { MatToolbar, MatToolbarRow } from '@angular/material/toolbar';
import { NgOptimizedImage } from '@angular/common';
import {Router, RouterLink} from '@angular/router';
import { MatIcon } from '@angular/material/icon';
import { MatFormField, MatPrefix } from '@angular/material/form-field';
import { MatInput } from '@angular/material/input';
import { NavbarMenuItemComponent } from '../navbar-menu-item/navbar-menu-item.component';
import { NavbarMenuComponent } from '../navbar-menu/navbar-menu.component';
import { UserRoles } from '../../model/user/userRoles';
import { NavButtonComponent } from '../nav-button/nav-button.component';
import { User } from '../../model/user/user';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {TranslateModule} from "@ngx-translate/core";


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
    ReactiveFormsModule,
    TranslateModule
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  router = inject(Router);

  showSearchBar = input(true);
  showRightBar = input(true);

  isLoggedIn = input<boolean | undefined>(undefined);
  loggedUser = input<User | null | undefined>(undefined);
  pfp = input<string>('');


  logout = output<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      search: ['', [Validators.required]]
    })
  }

  onSubmit(){
    if (this.form.valid){
      this.router.navigate(['/search'], {queryParams: {'title': this.form.get('search')?.value}})
    }
  }

  protected readonly UserRoles = UserRoles;
}
