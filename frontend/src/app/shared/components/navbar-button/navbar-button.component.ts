import {Component, input} from '@angular/core';
import {MatFabAnchor} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-navbar-button',
	imports: [
		MatFabAnchor,
		RouterLink
	],
  templateUrl: './navbar-button.component.html',
  styleUrl: './navbar-button.component.scss'
})
export class NavbarButtonComponent {
	link = input.required<string>();
}
