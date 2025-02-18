import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, output} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatIconModule} from '@angular/material/icon';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {Router, RouterModule} from '@angular/router';
import {NotificationCardComponent} from "../../../../shared/components/notification-card/notification-card.component";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {UserService} from "../../../../shared/services/user.service";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
	selector: 'app-register-form',
	standalone: true,
	imports: [
		CommonModule,
		MatFormFieldModule,
		MatInputModule,
		MatButtonModule,
		MatIconModule,
		ReactiveFormsModule,
		RouterModule,
		NotificationCardComponent,
		ActionButtonComponent,
		TranslateModule
	],
	templateUrl: './register-form.component.html',
	styleUrl: './register-form.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class RegisterFormComponent {
	userService = inject(UserService);


	signupForm: FormGroup;
	hide = true;
	hideRepeat = true;

	submitted = output<string>();

	constructor(private fb: FormBuilder, private router: Router, private translate: TranslateService) {
		this.signupForm = this.fb.group({
			firstName: ['', [Validators.required, Validators.maxLength(255)]],
			lastName: ['', [Validators.required, Validators.maxLength(255)]],
			email: ['', [Validators.required, Validators.email, Validators.maxLength(255)]],
			password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]],
			repeatPassword: ['', [Validators.required]]
		}, { validator: this.checkPasswords });
	}

	checkPasswords(group: FormGroup) {
		const password = group.get('password')?.value;
		const confirmPassword = group.get('repeatPassword')?.value;
		return password === confirmPassword ? null : { notMatching: true };
	}

	onSubmit() {
		if (this.signupForm.valid) {
			this.userService.postUser({
				firstName: this.signupForm.get('firstName')?.value,
				lastName: this.signupForm.get('lastName')?.value,
				email: this.signupForm.get('email')?.value,
				password: this.signupForm.get('password')?.value
			}).subscribe(() => {
				this.submitted.emit(this.signupForm.get('email')?.value)
			})
		}
	}

	showErrorMessage(field: string){
		return this.signupForm.get(field)?.invalid && this.signupForm.get(field)?.touched
	}

	getErrorMessage(field: string): string {
		if (this.signupForm.get(field)?.hasError('required')) {
			return this.translate.instant('SIGNUP.ERRORS.REQUIRED');
		}
		if (this.signupForm.get(field)?.hasError('email')) {
			return this.translate.instant('SIGNUP.ERRORS.INVALID_EMAIL');
		}
		if (this.signupForm.get(field)?.hasError('minlength')) {
			return this.translate.instant('SIGNUP.ERRORS.MIN_LENGTH');
		}
		if (this.signupForm.get(field)?.hasError('maxlength')) {
			return this.translate.instant('SIGNUP.ERRORS.MAX_LENGTH');
		}
		if (this.signupForm.hasError('notMatching')) {
			return this.translate.instant('SIGNUP.ERRORS.PASSWORD_MISMATCH');
		}
		return '';
	}


}

