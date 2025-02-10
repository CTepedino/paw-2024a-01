import {Component} from '@angular/core';
import {MatToolbar, MatToolbarRow} from "@angular/material/toolbar";
import {NgOptimizedImage} from "@angular/common";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatPrefix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NavbarMenuItemComponent} from "../navbar-menu-item/navbar-menu-item.component";
import {NavbarMenuComponent} from "../navbar-menu/navbar-menu.component";
import {UserRoles} from "../../model/user/userRoles";
import {NavButtonComponent} from "../nav-button/nav-button.component";

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
  showSearchBar = true;
  showRightBar = true;
  isLoggedIn = true;

  user = {
    id: 1,
    firstName: "John",
    lastName: "Doe",
    profilePicture: "assets/cybrary.png",
    roles: [UserRoles.READER, UserRoles.WRITER]
  };
  protected readonly UserRoles = UserRoles;
}
