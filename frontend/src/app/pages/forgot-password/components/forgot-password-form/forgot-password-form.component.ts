import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, output, signal} from '@angular/core';
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {NotificationCardComponent} from "../../../../shared/components/notification-card/notification-card.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {AuthService} from "../../../../shared/services/auth.service";
import {catchError, map, throwError} from "rxjs";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-forgot-password-form',
  imports: [
    ActionButtonComponent,
    MatFormField,
    MatInput,
    MatLabel,
    NotificationCardComponent,
    ReactiveFormsModule,
    TranslateModule
  ],
  templateUrl: './forgot-password-form.component.html',
  styleUrl: './forgot-password-form.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ForgotPasswordFormComponent {

  authService = inject(AuthService);

  emailSent = output<void>()

  submitFailed = signal(false);

  resetForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  submitReset(){
    const email = this.resetForm.get('email')?.value;

    this.authService.sendResetPasswordCodeEmail(email ?? '').pipe(
        map(() => {
          this.emailSent.emit();
        }),
        catchError(() => {
          this.submitFailed.set(true);
          this.resetForm.reset();
          return throwError(() => new Error("login failed"))
        })
    ).subscribe();
  }
}
