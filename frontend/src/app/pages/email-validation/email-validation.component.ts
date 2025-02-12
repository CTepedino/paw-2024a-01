import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {
  ActionNotificationCardComponent
} from "../../shared/components/action-notification-card/action-notification-card.component";


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

}
