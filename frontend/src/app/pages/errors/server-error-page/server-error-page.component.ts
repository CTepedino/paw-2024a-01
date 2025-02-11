import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {NotificationCardComponent} from "../../../shared/components/notification-card/notification-card.component";

@Component({
  selector: 'app-server-error-page',
	imports: [
		NotificationCardComponent
	],
  templateUrl: './server-error-page.component.html',
  styleUrl: './server-error-page.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServerErrorPageComponent {

}
