import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {
	ActionNotificationCardComponent
} from "../../../shared/components/action-notification-card/action-notification-card.component";

@Component({
  selector: 'app-server-error-page',
	imports: [
		ActionNotificationCardComponent
	],
  templateUrl: './server-error-page.component.html',
  styleUrl: './server-error-page.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServerErrorPageComponent {

}
