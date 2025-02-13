import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {NotificationCardComponent} from "../../../../shared/components/notification-card/notification-card.component";

@Component({
  selector: 'app-register-confirmation',
  imports: [
    NotificationCardComponent
  ],
  templateUrl: './register-confirmation.component.html',
  styleUrl: './register-confirmation.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RegisterConfirmationComponent {

}
