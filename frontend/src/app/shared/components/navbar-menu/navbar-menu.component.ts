import {Component, input} from '@angular/core';
import {MatMenu, MatMenuTrigger} from "@angular/material/menu";
import {NavbarMenuItemComponent} from "../navbar-menu-item/navbar-menu-item.component";
import {MatButton} from "@angular/material/button";
import {MatCardAvatar} from "@angular/material/card";
import {NgOptimizedImage} from "@angular/common";
import {User} from "../../model/user/user";

@Component({
  selector: 'app-navbar-menu',
  imports: [
    MatMenu,
    NavbarMenuItemComponent,
    MatMenuTrigger,
    MatButton,
    MatCardAvatar,
    NgOptimizedImage
  ],
  templateUrl: './navbar-menu.component.html',
  styleUrl: './navbar-menu.component.scss'
})
export class NavbarMenuComponent {
  user = input.required<User>()
}
