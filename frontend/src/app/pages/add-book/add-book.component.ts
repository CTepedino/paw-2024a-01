import {Component, inject, OnInit} from '@angular/core';
import {ContentCardComponent} from "../../shared/components/content-card/content-card.component";
import {MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatOption, MatSelect} from "@angular/material/select";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {DateAdapter, MAT_DATE_FORMATS, MAT_NATIVE_DATE_FORMATS, NativeDateAdapter} from "@angular/material/core";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {FileInputComponent} from "../../shared/components/file-input/file-input.component";
import {AddBookService} from "./store/add-book.service";
import {AsyncPipe} from "@angular/common";
import {fileTypeValidator} from "../../shared/validators/fileTypeValidator";
import {Router} from "@angular/router";
import {catchError, map, throwError} from "rxjs";
import {Title} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";


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
    ActionButtonComponent,
    FileInputComponent,
    AsyncPipe,
    TranslateModule,
  ],
  templateUrl: './add-book.component.html',
  styleUrl: './add-book.component.scss',
  providers: [
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}
  ]
})
export class AddBookComponent implements OnInit {
  title = inject(Title);
  addBookService = inject(AddBookService);
  router = inject(Router);

  protected readonly BookGenre = BookGenre;
  protected readonly Object = Object;

  maxDate = new Date();

  showCBUField = this.addBookService.shouldShowCBUField();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.title.setTitle('Publish')

    this.form = this.fb.group({
      cbu: [''],
      title: ['', [Validators.required, Validators.maxLength(50)]],
      description: ['', [Validators.required, Validators.maxLength(1000)]],
      genre: [null, [Validators.required]],
      suggestedAge: [null, [Validators.required, Validators.max(100), Validators.min(0)]],
      price: [null, [Validators.required, Validators.max(100000000), Validators.min(0.1)]],
      pageCount: [null, [Validators.required, Validators.max(1000000), Validators.min(0)]],
      publicationDate: [null, [Validators.required]],
      cover: this.fb.group({
        fileName: [''],
        fileData: [null, [Validators.required, fileTypeValidator(['image/*'])]],
      }),
      preview: this.fb.group({
        fileName: [''],
        fileData: [null, [Validators.required, fileTypeValidator(['application/pdf'])]],
      }),
      file: this.fb.group({
        fileName: [''],
        fileData: [null, [Validators.required, fileTypeValidator(['application/pdf'])]],
      }),
    });
  }

  ngOnInit() {
    this.showCBUField.pipe(map((show) => {
      if (show){
        this.form.get('cbu')?.addValidators([
            Validators.required,
            Validators.pattern(/^[a-zA-ZáéíóüúÁÉÍÓÜÚ0-9ñÑ.-]+$/),
            Validators.minLength(6),
            Validators.maxLength(22)
        ]);
        this.form.get('cbu')?.updateValueAndValidity();
      }
    })).subscribe();
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
    if (this.form.valid){

      this.addBookService.publish(this.form).pipe(
      map((id) => {
        console.log(`id: ${id}`)
        this.router.navigate([`/book/${id}`]);
      }), catchError((err) => {
        console.log(err);
        return throwError(() => 'publish failed');
      })
      ).subscribe();
    }
  }



}
