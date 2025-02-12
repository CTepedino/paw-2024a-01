import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, output, signal} from '@angular/core';
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NotificationCardComponent} from "../../../../shared/components/notification-card/notification-card.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router} from "@angular/router";
import {ResetPasswordService} from "../../store/reset-password.service";
import {catchError, map, throwError} from "rxjs";

@Component({
  selector: 'app-reset-password-form',
	imports: [
		ActionButtonComponent,
		MatFormField,
		MatIcon,
		MatIconButton,
		MatInput,
		MatLabel,
		MatSuffix,
		NotificationCardComponent,
		ReactiveFormsModule
	],
  templateUrl: './reset-password-form.component.html',
  styleUrl: './reset-password-form.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ResetPasswordFormComponent {
	resetPasswordService = inject(ResetPasswordService);
	router = inject(Router);

	success = output<void>()

	resetFailed = signal(false);
	noMatch = signal(false);

	hide = signal(true);
	toggleHide(event: MouseEvent) {
		event.preventDefault();
		this.hide.set(!this.hide());
		event.stopPropagation();
	}

	resetForm = new FormGroup({
		password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]),
		confirm: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]),
	});

	resetPassword(){
		if (this.resetForm.valid) {
			this.noMatch.set(false);
			const password = this.resetForm.get('password')?.value;
			const confirm = this.resetForm.get('confirm')?.value;

			if (password == confirm) {
				this.resetPasswordService.resetPassword(password ?? '').pipe(
					map(index => {
						this.success.emit()
					}),
					catchError(() => {
						this.resetFailed.set(true);
						this.resetForm.reset();
						return throwError(() => new Error("reset password failed"))
					})
				).subscribe();
			} else {
				this.noMatch.set(true);
			}
		}
	}
}
