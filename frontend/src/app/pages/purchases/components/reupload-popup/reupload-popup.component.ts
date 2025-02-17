import { Component } from '@angular/core';
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatDialogActions, MatDialogContent, MatDialogRef, MatDialogTitle} from "@angular/material/dialog";
import {fileTypeValidator} from "../../../../shared/validators/fileTypeValidator";
import {MatButton} from "@angular/material/button";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FileInputComponent} from "../../../../shared/components/file-input/file-input.component";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-reupload-popup',
  imports: [
    FormsModule,
    MatButton,
    MatDialogActions,
    MatDialogContent,
    MatDialogTitle,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    FileInputComponent,
    TranslateModule
  ],
  templateUrl: './reupload-popup.component.html',
  styleUrl: './reupload-popup.component.scss'
})
export class ReuploadPopupComponent {
  form: FormGroup;

  constructor(public dialogRef: MatDialogRef<ReuploadPopupComponent>, private fb: FormBuilder) {
    this.form = fb.group({
      fileName: [],
      fileData: [null, [Validators.required, fileTypeValidator(['image/*', 'application/pdf'])]]
    })
  }

  closeDialog() {
    this.dialogRef.close();
  }

  onSubmit(){
    if (this.form.valid){
      this.dialogRef.close({file: this.form.get('fileData')?.value})
    }
  }

  protected readonly FormGroup = FormGroup;
}
