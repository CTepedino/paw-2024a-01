import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {PasswordInputComponent} from "../../shared/components/password-input/password-input.component";
import {NotificationCardComponent} from "../../shared/components/notification-card/notification-card.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {FormControl, ReactiveFormsModule} from "@angular/forms";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../shared/services/auth.service";
import {map, tap} from "rxjs";

@Component({
  selector: 'app-login',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    PasswordInputComponent,
    NotificationCardComponent,
    MatCheckbox,
    ReactiveFormsModule,
    ActionButtonComponent,
    RouterLink
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginComponent {

  authService = inject(AuthService);
  router = inject(Router);

  email = new FormControl<string>('', {nonNullable: true});
  password = new FormControl<string>('', {nonNullable: true});
  rememberMe = new FormControl<boolean>(false, {nonNullable: true});

  doLogin(){
    this.authService.login(this.email.value, this.password.value, this.rememberMe.value).pipe(
        map( index => {
          if (index.loggedUser) {
            this.router.navigate(['/']);
          }
        })
    ).subscribe();

  }
}
