import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {PasswordInputComponent} from "../../shared/components/password-input/password-input.component";
import {NotificationCardComponent} from "../../shared/components/notification-card/notification-card.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {FormsModule} from "@angular/forms";
import {MatButton, MatFabButton} from "@angular/material/button";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-login',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    PasswordInputComponent,
    NotificationCardComponent,
    MatCheckbox,
    FormsModule,
    MatButton,
    MatFabButton,
    ActionButtonComponent,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginComponent {

  rememberMe = false;
}
