import {booleanAttribute, Component, input} from '@angular/core';
import {MatIcon} from "@angular/material/icon";
import {MatMenuItem} from "@angular/material/menu";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-navbar-menu-item',
  imports: [
    MatIcon,
    MatMenuItem,
    RouterLink
  ],
  templateUrl: './navbar-menu-item.component.html',
  styleUrl: './navbar-menu-item.component.scss'
})
export class NavbarMenuItemComponent {
  link = input.required<string>();
  icon = input.required<string>();
  last = input(false, {transform: booleanAttribute});
}
