import {Component, input, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {NotificationCardComponent} from "../notification-card/notification-card.component";
import {NavButtonComponent} from "../nav-button/nav-button.component";

@Component({
  selector: 'app-action-notification-card',
	imports: [
		NotificationCardComponent,
		NavButtonComponent
	],
  templateUrl: './action-notification-card.component.html',
  styleUrl: './action-notification-card.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ActionNotificationCardComponent {
	link = input.required<string>();
}
