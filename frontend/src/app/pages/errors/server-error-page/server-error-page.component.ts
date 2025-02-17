import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {
	ActionNotificationCardComponent
} from "../../../shared/components/action-notification-card/action-notification-card.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-server-error-page',
	imports: [
		ActionNotificationCardComponent,
		TranslateModule
	],
  templateUrl: './server-error-page.component.html',
  styleUrl: './server-error-page.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ServerErrorPageComponent {
	title = inject(Title);

	constructor(private translate: TranslateService) {
		this.translate.get('SERVER_ERROR').subscribe(translatedTitle => {
			this.title.setTitle(translatedTitle);
		});

	}
}
