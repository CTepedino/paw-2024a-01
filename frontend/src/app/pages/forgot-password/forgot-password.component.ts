import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {ForgotPasswordFormComponent} from "./components/forgot-password-form/forgot-password-form.component";
import {
  ForgotPasswordMailSucessComponent
} from "./components/forgot-password-mail-sucess/forgot-password-mail-sucess.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-forgot-password',
  imports: [
    ReactiveFormsModule,
    ForgotPasswordFormComponent,
    ForgotPasswordMailSucessComponent,
    TranslateModule
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ForgotPasswordComponent {
  title = inject(Title);

  constructor() {
    this.title.setTitle('Forgot Password')
  }

  showForm = true;

  switchView(){
    this.showForm = !this.showForm;
  }
}
