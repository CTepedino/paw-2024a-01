import {Component, input} from '@angular/core';
import {RouterLink} from "@angular/router";
import {ActionButtonComponent} from "../action-button/action-button.component";

@Component({
  selector: 'app-nav-button',
	imports: [
		RouterLink,
		ActionButtonComponent
	],
  templateUrl: './nav-button.component.html',
  styleUrl: './nav-button.component.scss'
})
export class NavButtonComponent {
	link = input.required<string>();
}
