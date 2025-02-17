import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, signal} from '@angular/core';
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatIconButton} from "@angular/material/button";
import {MatInput} from "@angular/material/input";
import {NotificationCardComponent} from "../../shared/components/notification-card/notification-card.component";
import {FormControl, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {catchError, concatMap, map, of, throwError} from "rxjs";
import {ChangePasswordService} from "./store/change-password.service";
import {Title} from "@angular/platform-browser";
import {CancelButtonComponent} from "../../shared/components/cancel-button/cancel-button.component";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-change-password',
	imports: [
		ActionButtonComponent,
		MatFormField,
		MatIcon,
		MatIconButton,
		MatInput,
		MatLabel,
		MatSuffix,
		NotificationCardComponent,
		ReactiveFormsModule,
		CancelButtonComponent,
		RouterLink,
		TranslateModule
	],
  templateUrl: './change-password.component.html',
  styleUrl: './change-password.component.scss',
	schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ChangePasswordComponent {
	title = inject(Title);
	changePasswordService = inject(ChangePasswordService)
	router = inject(Router);

	changeFailed = signal(false);
	noMatch = signal(false);
	authFailure = signal(false);


	hideOld = true;
	hideNew = true;
	hideRepeat = true;

	changePasswordForm = new FormGroup({
		old: new FormControl('', [Validators.required]),
		password: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]),
		confirm: new FormControl('', [Validators.required, Validators.minLength(6), Validators.maxLength(255)]),
	});

	constructor() {
		this.title.setTitle('Change password');
	}

	changePassword(){
		if (this.changePasswordForm.valid) {

			this.noMatch.set(false);
			this.changeFailed.set(false);
			this.authFailure.set(false);

			const oldPassword = this.changePasswordForm.get('old')?.value;
			const password = this.changePasswordForm.get('password')?.value;
			const confirm = this.changePasswordForm.get('confirm')?.value;

			if (password == confirm) {

				this.changePasswordService.validateOld(oldPassword ?? '').pipe(
					concatMap(matches => {
						if (!matches){
							this.authFailure.set(true);
							return of(null);
						} else {
							return this.changePasswordService.resetPassword(password ?? '').pipe(
								map(() => {
									this.router.navigate(['/profile'])
								}),
								catchError(() => {
									this.changeFailed.set(true);
									this.changePasswordForm.reset();
									return throwError(() => new Error("change password failed"))
								})
							)
						}
					})
				).subscribe();

			} else {
				this.noMatch.set(true);
			}
		}
	}
}
