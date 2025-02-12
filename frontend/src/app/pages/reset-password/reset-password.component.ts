import {Component} from '@angular/core';
import {ResetPasswordSucessComponent} from "./components/reset-password-sucess/reset-password-sucess.component";
import {ResetPasswordFormComponent} from "./components/reset-password-form/reset-password-form.component";

@Component({
  selector: 'app-reset-password',
	imports: [
		ResetPasswordSucessComponent,
		ResetPasswordFormComponent
	],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.scss'
})
export class ResetPasswordComponent {
	resetting = true;

	showSuccess(){
		this.resetting = !this.resetting;
	}
}
