import {Component} from '@angular/core';
import {MatToolbar, MatToolbarRow} from "@angular/material/toolbar";
import {NgOptimizedImage} from "@angular/common";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatButton, MatFabAnchor} from "@angular/material/button";
import {MatFormField, MatPrefix, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatMenu, MatMenuItem, MatMenuTrigger} from "@angular/material/menu";
import {MatCardAvatar} from "@angular/material/card";

@Component({
  selector: 'app-navbar',
  imports: [
    MatToolbar,
    MatToolbarRow,
    NgOptimizedImage,
    RouterLink,
    MatIcon,
    MatFabAnchor,
    MatFormField,
    MatPrefix,
    MatInput,
    MatLabel,
    MatButton,
    MatMenu,
    MatMenuItem,
    MatMenuTrigger,
    MatCardAvatar
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.scss'
})
export class NavbarComponent {
  showSearchBar = true;
  showRightBar = true;
  isLoggedIn = true;

  user = {
    userId: 1,
    firstName: "John",
    lastName: "Doe",
    profilePicture: "assets/cybrary.png",
    roles: ['READER', 'WRITER']
  };
}
