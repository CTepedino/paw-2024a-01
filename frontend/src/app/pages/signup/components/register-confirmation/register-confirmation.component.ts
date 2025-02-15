import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, input} from '@angular/core';
import {NotificationCardComponent} from "../../../../shared/components/notification-card/notification-card.component";
import {AuthService} from "../../../../shared/services/auth.service";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";

@Component({
  selector: 'app-register-confirmation',
  imports: [
    NotificationCardComponent,
    ActionButtonComponent
  ],
  templateUrl: './register-confirmation.component.html',
  styleUrl: './register-confirmation.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RegisterConfirmationComponent {
  authService = inject(AuthService);

  email = input<string>('');
  disableResend = false;

  resendEmail(){
    this.disableResend = true;
    setTimeout(() => {
      this.disableResend = false;
    }, 30000);
    this.authService.resendVerificationCodeEmail(this.email()).subscribe();
  }
}
