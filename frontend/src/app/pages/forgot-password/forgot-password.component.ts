import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {ReactiveFormsModule} from "@angular/forms";
import {ForgotPasswordFormComponent} from "./components/forgot-password-form/forgot-password-form.component";
import {
  ForgotPasswordMailSucessComponent
} from "./components/forgot-password-mail-sucess/forgot-password-mail-sucess.component";

@Component({
  selector: 'app-forgot-password',
  imports: [
    ReactiveFormsModule,
    ForgotPasswordFormComponent,
    ForgotPasswordMailSucessComponent
  ],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ForgotPasswordComponent {
  showForm = true;

  switchView(){
    this.showForm = !this.showForm;
  }
}
