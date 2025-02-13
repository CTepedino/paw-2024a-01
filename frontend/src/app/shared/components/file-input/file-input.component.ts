import {Component, Input, input} from '@angular/core';
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatButton} from "@angular/material/button";
import {FormGroup, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-file-input',
  imports: [
    MatLabel,
    MatFormField,
    MatInput,
    MatButton,
    ReactiveFormsModule,
    MatSuffix
  ],
  templateUrl: './file-input.component.html',
  styleUrl: './file-input.component.scss'
})
export class FileInputComponent {
  @Input() formGroup!: FormGroup;

  accept = input.required<string>();
  required = input(true);

  onFileSelected(event: Event){
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      const file = input.files[0];

      this.formGroup.patchValue({
        fileName: file.name,
        fileData: file,
      });

      this.formGroup.get('fileName')?.updateValueAndValidity();
    }
  }
}
