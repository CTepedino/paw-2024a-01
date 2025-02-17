import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {NotificationCardComponent} from "../../../../shared/components/notification-card/notification-card.component";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-forgot-password-mail-sucess',
  imports: [
    NotificationCardComponent,
    TranslateModule
  ],
  templateUrl: './forgot-password-mail-sucess.component.html',
  styleUrl: './forgot-password-mail-sucess.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ForgotPasswordMailSucessComponent {

}
