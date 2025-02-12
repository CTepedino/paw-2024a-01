import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, signal} from '@angular/core';
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";

import {NotificationCardComponent} from "../../shared/components/notification-card/notification-card.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {FormBuilder, FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../shared/services/auth.service";
import {catchError, map, tap, throwError} from "rxjs";
import {F} from "@angular/cdk/keycodes";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";

@Component({
  selector: 'app-login',
  imports: [
    MatFormField,
    MatInput,
    MatLabel,
    NotificationCardComponent,
    MatCheckbox,
    ReactiveFormsModule,
    ActionButtonComponent,
    RouterLink,
    MatIcon,
    MatIconButton,
    MatSuffix
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginComponent {

  authService = inject(AuthService);
  router = inject(Router);

  loginFailed = signal(false);

  hide = signal(true);
  toggleHide(event: MouseEvent) {
    event.preventDefault();
    this.hide.set(!this.hide());
    event.stopPropagation();
  }

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required]),
    rememberMe: new FormControl(false)
  });

  doLogin(){
    const email = this.loginForm.get('email')?.value;
    const password = this.loginForm.get('password')?.value;
    const rememberMe = this.loginForm.get('rememberMe')?.value;

    this.authService.login(email ?? '', password ?? '', rememberMe ?? false).pipe(
        map( index => {
          if (index.loggedUser) {
            this.router.navigate(['/']);
          } else {
            this.loginFailed.set(true);
          }
        }),
        catchError(() => {
          this.loginFailed.set(true);
          this.loginForm.reset();
          return throwError(() => new Error("login failed"))
        })
    ).subscribe();

  }

}
