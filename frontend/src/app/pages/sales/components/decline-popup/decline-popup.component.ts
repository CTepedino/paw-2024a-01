import {Component, output} from '@angular/core';
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatButtonModule} from "@angular/material/button";
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-decline-popup',
  imports: [MatFormFieldModule,
    MatInputModule,
    FormsModule,
    MatButtonModule,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,
    ReactiveFormsModule,
    TranslateModule],
  templateUrl: './decline-popup.component.html',
  styleUrl: './decline-popup.component.scss'
})
export class DeclinePopupComponent {
  form: FormGroup;

  constructor(public dialogRef: MatDialogRef<DeclinePopupComponent>, private fb: FormBuilder) {
    this.form = fb.group({
      reason: ['', [Validators.required, Validators.maxLength(255)]]
    })
  }

  closeDialog() {
    this.dialogRef.close();
  }

  onSubmit(){
    if (this.form.valid){
      this.dialogRef.close({reason: this.form.get('reason')?.value})
    }
  }
}
