import {Component, inject, input, output} from '@angular/core';
import {map} from "rxjs";
import {ActionButtonComponent} from "../../../../../../shared/components/action-button/action-button.component";
import {FileInputComponent} from "../../../../../../shared/components/file-input/file-input.component";
import {ContentCardComponent} from "../../../../../../shared/components/content-card/content-card.component";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {CurrencyPipe} from "@angular/common";
import {CancelButtonComponent} from "../../../../../../shared/components/cancel-button/cancel-button.component";
import {RouterLink} from "@angular/router";
import {BookDetailsService} from "../../../../store/book-details.service";
import {BookWithData} from "../../../../../../shared/model/book/bookWithData";
import {fileTypeValidator} from "../../../../../../shared/validators/fileTypeValidator";
import {TranslateModule} from "@ngx-translate/core";


@Component({
  selector: 'app-buy-book-form',
  imports: [
    ContentCardComponent,
    ReactiveFormsModule,
    CurrencyPipe,
    FileInputComponent,
    ActionButtonComponent,
    CancelButtonComponent,
    RouterLink,
    TranslateModule
  ],
  templateUrl: './buy-book-form.component.html',
  styleUrl: './buy-book-form.component.scss'
})
export class BuyBookFormComponent {
  private bookDetailsService = inject(BookDetailsService);

  bought = output<void>()

  book = input.required<BookWithData>();

  form!: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      receipt: this.fb.group({
        fileName: [''],
        fileData: [null, [Validators.required, fileTypeValidator(['image/*', 'application/pdf'])]],
      }),
    });
  }

  getPrice(book: BookWithData){
    return book.dealInfo ? book.dealInfo.price : book.price;
  }

  getReceiptFormGroup(): FormGroup{
    return this.form.get('receipt') as FormGroup;
  }


  onSubmit() {
    if (this.form.valid){
      this.bookDetailsService.buy(this.book().id!, this.form.get('receipt')?.get('fileData')?.value).pipe(
          map(() => {
            this.bought.emit()
          })
      ).subscribe();
    }
  }
}
