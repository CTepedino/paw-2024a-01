import {Component, inject, OnInit} from '@angular/core';
import {ContentCardComponent} from "../../../../shared/components/content-card/content-card.component";
import {BookWithData} from "../../../../shared/model/book/bookWithData";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {catchError, map, Observable, tap, throwError} from "rxjs";
import {BookDetailsService} from "../../store/book-details.service";
import {AsyncPipe, CurrencyPipe, DecimalPipe} from "@angular/common";
import {MatFormField, MatHint, MatLabel, MatSuffix} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatDatepicker, MatDatepickerInput, MatDatepickerToggle} from "@angular/material/datepicker";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {CancelButtonComponent} from "../../../../shared/components/cancel-button/cancel-button.component";
import {DateAdapter, MAT_DATE_FORMATS, MAT_NATIVE_DATE_FORMATS, NativeDateAdapter} from "@angular/material/core";
import {DeleteButtonComponent} from "../../../../shared/components/delete-button/delete-button.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-deal',
  imports: [
    ContentCardComponent,
    AsyncPipe,
    CurrencyPipe,
    MatFormField,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatDatepicker,
    MatDatepickerInput,
    MatDatepickerToggle,
    MatHint,
    MatSuffix,
    ActionButtonComponent,
    CancelButtonComponent,
    RouterLink,
    DecimalPipe,
    DeleteButtonComponent,
    TranslateModule
  ],
  templateUrl: './deal.component.html',
  styleUrl: './deal.component.scss',
  providers: [
    {provide: DateAdapter, useClass: NativeDateAdapter},
    {provide: MAT_DATE_FORMATS, useValue: MAT_NATIVE_DATE_FORMATS}
  ]
})
export class DealComponent implements OnInit {
  route = inject(ActivatedRoute);
  router = inject(Router);
  bookDetailsService = inject(BookDetailsService);
  title = inject(Title);

  id: any;
  book$: Observable<BookWithData> | null  = null;

  form: FormGroup;

  minDate: Date;

  constructor(private fb: FormBuilder, private translate: TranslateService) {
    this.translate.get('SET_DEAL_BROWSER').subscribe(translatedTitle => {
      this.title.setTitle(translatedTitle);
    });

    this.minDate = new Date();
    this.minDate.setTime(new Date().getTime() + 24 * 60 * 60 * 1000);

    this.form = this.fb.group({
      price: [null, [Validators.required, Validators.max(100000000), Validators.min(0.1)]],
      endDate: [null, [Validators.required]],
    });
  }

  getMinPrice(price: number): number{
    return price*0.95;
  }


  ngOnInit() {
    this.id = this.route.snapshot.paramMap.get('id');
    this.book$ = this.bookDetailsService.getBook(this.id!).pipe(
        tap((book) => {
          if (book.dealInfo){
            this.form.get('price')?.setValue(book.dealInfo.price);
            this.form.get('endDate')?.setValue(new Date(book.dealInfo.end!));
          }

          this.form.get('price')?.addValidators(Validators.max(this.getMinPrice(book.price!)))
          this.form.get('price')?.updateValueAndValidity();
        })
    );
  }

  onSubmit(){
    if (this.form.valid){

      this.bookDetailsService.setDeal(this.id, this.form).pipe(
          map(() => {
            this.router.navigate([`/book/${this.id}`]);
          }), catchError((err) => {
            console.log(err);
            return throwError(() => 'deal failed');
          })
      ).subscribe();
    }
  }

  endDeal(){
    this.bookDetailsService.endDeal(this.id).pipe(
        map(() => {
          this.router.navigate([`/book/${this.id}`]);
        })
    ).subscribe();
  }
}
