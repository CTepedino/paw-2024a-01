import {Component, inject} from '@angular/core';
import {ResetPasswordSucessComponent} from "./components/reset-password-sucess/reset-password-sucess.component";
import {ResetPasswordFormComponent} from "./components/reset-password-form/reset-password-form.component";
import {Title} from "@angular/platform-browser";

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
	title = inject(Title);

	constructor() {
		this.title.setTitle('Reset Password')
	}

	resetting = true;

	showSuccess(){
		this.resetting = !this.resetting;
	}
}
