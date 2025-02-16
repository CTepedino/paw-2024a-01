import {Component, CUSTOM_ELEMENTS_SCHEMA, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatIconModule} from '@angular/material/icon';
import {NotificationCardComponent} from "../../shared/components/notification-card/notification-card.component";
import {FileInputComponent} from "../../shared/components/file-input/file-input.component";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {fileTypeValidator} from "../../shared/validators/fileTypeValidator";
import {map} from "rxjs";
import {ProfileEditService} from "./store/profile-edit.service";
import {UserRoles} from "../../shared/model/user/userRoles";
import {CancelButtonComponent} from "../../shared/components/cancel-button/cancel-button.component";
import {Router, RouterLink} from "@angular/router";

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule,
    NotificationCardComponent,
    FileInputComponent,
    ActionButtonComponent,
    CancelButtonComponent,
    RouterLink
  ],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EditProfileComponent implements OnInit {
  profileEditService = inject(ProfileEditService);
  router = inject(Router);

  shouldShowCbu = false;

  editForm: FormGroup;
  userId: any;

  constructor(private fb: FormBuilder) {
    this.editForm = this.fb.group({
      firstName: ['', [Validators.required, Validators.maxLength(50)]],
      lastName: ['', [Validators.required, Validators.maxLength(50)]],
      cbu: [''],
      description: ['', [Validators.maxLength(500)]],
      pfp: this.fb.group({
        fileName: [''],
        fileData: [null, [fileTypeValidator(['image/*'])]],
      })
    });
  }

  ngOnInit(): void {
    this.profileEditService.getLoggedUser().pipe(
        map(user => {
          if (user?.roles?.includes(UserRoles.WRITER)){
            this.shouldShowCbu = true;
            this.editForm.get('cbu')?.setValidators(
                [Validators.required, Validators.minLength(6), Validators.maxLength(22), Validators.pattern(/^[a-zA-ZáéíóüúÁÉÍÓÜÚ0-9ñÑ.-]+$/)]
            )
            this.editForm.get('cbu')?.setValue(user?.cbu);
          }
          this.editForm.get('firstName')?.setValue(user?.firstName);
          this.editForm.get('lastName')?.setValue(user?.lastName);
          this.editForm.get('description')?.setValue(user?.description);
          this.editForm.updateValueAndValidity();
          this.userId = user?.id;
        })
    ).subscribe();
  }



  onSubmit() {
    if (this.editForm.valid) {
      this.profileEditService.updateProfile(this.editForm).pipe(
          map(() => {
            this.router.navigate([`profile/${this.userId}`]);
          })
      ).subscribe();
    }
  }

  getPfpFormGroup(){
    return this.editForm.get('pfp') as FormGroup;
  }

  getErrorMessage(field: string): string {
    if (this.editForm.get(field)?.hasError('required')) {
      return 'This field is required';
    }
    if (this.editForm.get(field)?.hasError('maxlength')) {
      return 'Exceeds the maximum permitted length';
    }
    if (this.editForm.get(field)?.hasError('pattern')) {
      return 'Invalid cbu or alias';
    }
    return '';
  }
}