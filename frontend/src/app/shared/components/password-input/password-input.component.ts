import {Component, input, signal} from '@angular/core';
import {MatFormField, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {MatIconButton} from "@angular/material/button";
import {FormControl, ReactiveFormsModule} from "@angular/forms";

@Component({
  selector: 'app-password-input',
  imports: [
    MatFormField,
    MatIcon,
    MatLabel,
    MatInput,
    MatIconButton,
    MatSuffix,
    ReactiveFormsModule
  ],
  templateUrl: './password-input.component.html',
  styleUrl: './password-input.component.scss'
})
export class PasswordInputComponent {
  control = input.required<FormControl>();

  hide = signal(true);

  clickEvent(event: MouseEvent) {
    this.hide.set(!this.hide());
    event.stopPropagation();
  }
}
