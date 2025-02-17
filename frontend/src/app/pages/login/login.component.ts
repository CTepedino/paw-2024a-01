import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, input, signal} from '@angular/core';
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";

import {NotificationCardComponent} from "../../shared/components/notification-card/notification-card.component";
import {MatCheckbox} from "@angular/material/checkbox";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {Router, RouterLink} from "@angular/router";
import {AuthService} from "../../shared/services/auth.service";
import {catchError, map, throwError} from "rxjs";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {Title} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";

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
    MatSuffix,
    TranslateModule
  ],
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LoginComponent {
  title = inject(Title);
  authService = inject(AuthService);
  router = inject(Router);

  constructor() {
    this.title.setTitle('Login')
  }

  redirect = input<string>()

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
    if (this.loginForm.valid) {
      const email = this.loginForm.get('email')?.value;
      const password = this.loginForm.get('password')?.value;
      const rememberMe = this.loginForm.get('rememberMe')?.value;

      this.authService.login(email ?? '', password ?? '', rememberMe ?? false).pipe(
          map(index => {
            this.router.navigate([this.redirect() || '/']);
          }),
          catchError(() => {
            this.loginFailed.set(true);
            this.loginForm.reset();
            return throwError(() => new Error("login failed"))
          })
      ).subscribe();
    }
  }

}
