import {Component, ElementRef, input, ViewChild} from '@angular/core';
import {ContentCardComponent} from "../../shared/components/content-card/content-card.component";
import {MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatOption, MatSelect} from "@angular/material/select";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {DateAdapter, MAT_DATE_FORMATS, MAT_NATIVE_DATE_FORMATS, NativeDateAdapter} from "@angular/material/core";
import {MatIcon} from "@angular/material/icon";
import {MatButton} from "@angular/material/button";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {FileInputComponent} from "../../shared/components/file-input/file-input.component";

@Component({
  selector: 'app-add-book',
  imports: [
    ContentCardComponent,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatSelect,
    MatOption,
    MatDatepickerInput,
    MatHint,
    MatDatepickerToggle,
    MatDatepicker,
    MatSuffix,
    MatIcon,
    MatButton,
    ActionButtonComponent,
    FileInputComponent
  ],
  templateUrl: './add-book.component.html',
  styleUrl: './add-book.component.scss',
  providers: [
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}
  ]
})
export class AddBookComponent {

  firstPublication = input(false);

  protected readonly BookGenre = BookGenre;
  protected readonly Object = Object;

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      title: [''],
      cover: this.fb.group({
        fileName: [''],
        fileData: [null],
      }),
      preview: this.fb.group({
        fileName: [''],
        fileData: [null],
      }),
      file: this.fb.group({
        fileName: [''],
        fileData: [null],
      }),
    });
  }

  getCoverFormGroup(): FormGroup{
    return this.form.get('cover') as FormGroup;
  }

  getPreviewFormGroup(): FormGroup{
    return this.form.get('preview') as FormGroup;
  }

  getFileFormGroup(): FormGroup{
    return this.form.get('file') as FormGroup;
  }

  onSubmit() {
    const file = this.form.get('fileUpload.fileData')?.value;
    if (file) {
      console.log('Uploading file:', file);
      // Handle the file upload process
    }
  }

}
