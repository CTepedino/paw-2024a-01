import {Component, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader} from "@angular/material/card";
import {NavButtonComponent} from "../nav-button/nav-button.component";

@Component({
  selector: 'app-notification-card',
	imports: [
		MatCard,
		MatCardContent,
		MatCardHeader,
		NavButtonComponent
	],
  templateUrl: './notification-card.component.html',
  styleUrl: './notification-card.component.scss'
})
export class NotificationCardComponent {
	link = input.required<string>();
}
