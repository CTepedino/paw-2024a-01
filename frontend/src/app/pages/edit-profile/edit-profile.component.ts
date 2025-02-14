import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatIconModule } from '@angular/material/icon';

@Component({
  selector: 'app-edit-profile',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    ReactiveFormsModule,
    MatIconModule
  ],
  templateUrl: './edit-profile.component.html',
  styleUrl: './edit-profile.component.scss'
})
export class EditProfileComponent {
  editForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.editForm = this.fb.group({
      firstName: ['Federico', [Validators.required, Validators.maxLength(50)]],
      lastName: ['Madero', [Validators.required, Validators.maxLength(50)]],
      cbu: ['111122223333444555566', [Validators.required, Validators.maxLength(22), Validators.pattern('^[0-9]*$')]],
      description: ['', [Validators.maxLength(500)]],
      profileImage: [null]
    });
  }

  onSubmit() {
    if (this.editForm.valid) {
      console.log('Form data:', this.editForm.value);
    }
  }

  getErrorMessage(field: string): string {
    if (this.editForm.get(field)?.hasError('required')) {
      return 'Este campo es requerido';
    }
    if (this.editForm.get(field)?.hasError('maxlength')) {
      return 'Excede el largo máximo permitido';
    }
    if (this.editForm.get(field)?.hasError('pattern')) {
      return 'Solo se permiten números';
    }
    return '';
  }
}