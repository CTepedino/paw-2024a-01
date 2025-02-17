import { Component, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {
  ActionNotificationCardComponent
} from "../../../../shared/components/action-notification-card/action-notification-card.component";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-reset-password-sucess',
  imports: [
    ActionNotificationCardComponent,
    TranslateModule
  ],
  templateUrl: './reset-password-sucess.component.html',
  styleUrl: './reset-password-sucess.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResetPasswordSucessComponent {

}
