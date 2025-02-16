import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {
  ActionNotificationCardComponent
} from "../../../shared/components/action-notification-card/action-notification-card.component";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-not-found-page',
  imports: [
    ActionNotificationCardComponent
  ],
  templateUrl: './not-found-page.component.html',
  styleUrl: './not-found-page.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NotFoundPageComponent {
  title = inject(Title);

  constructor() {
    this.title.setTitle('Page not found');
  }
}
