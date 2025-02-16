import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {
  ActionNotificationCardComponent
} from "../../shared/components/action-notification-card/action-notification-card.component";
import {Title} from "@angular/platform-browser";


@Component({
  selector: 'app-email-validation',
  imports: [
    ActionNotificationCardComponent
  ],
  templateUrl: './email-validation.component.html',
  styleUrl: './email-validation.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EmailValidationComponent {
  title = inject(Title);

  constructor() {
    this.title.setTitle('Validate email');
  }
}
