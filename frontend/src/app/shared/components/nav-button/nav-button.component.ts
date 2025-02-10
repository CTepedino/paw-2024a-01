import {Component, input} from '@angular/core';
import {MatFabAnchor} from "@angular/material/button";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-nav-button',
	imports: [
		MatFabAnchor,
		RouterLink
	],
  templateUrl: './nav-button.component.html',
  styleUrl: './nav-button.component.scss'
})
export class NavButtonComponent {
	link = input.required<string>();
}
