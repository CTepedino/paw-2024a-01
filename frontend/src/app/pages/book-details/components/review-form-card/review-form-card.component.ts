import {Component, inject, OnInit} from '@angular/core';
import {ContentCardComponent} from "../../../../shared/components/content-card/content-card.component";
import {NgbRating} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {map, Observable, tap} from "rxjs";
import {Review} from "../../../../shared/model/review/review";
import {ActivatedRoute, Router, RouterLink} from "@angular/router";
import {BookDetailsService} from "../../store/book-details.service";
import {MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {CancelButtonComponent} from "../../../../shared/components/cancel-button/cancel-button.component";
import {DeleteButtonComponent} from "../../../../shared/components/delete-button/delete-button.component";

@Component({
  selector: 'app-review-form-card',
  imports: [
    ContentCardComponent,
    NgbRating,
    MatFormField,
    MatHint,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    ActionButtonComponent,
    CancelButtonComponent,
    DeleteButtonComponent,
    RouterLink,

  ],
  templateUrl: './review-form-card.component.html',
  styleUrl: './review-form-card.component.scss'
})
export class ReviewFormCardComponent implements OnInit {
  bookDetailsService = inject(BookDetailsService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  id: string;
  review$: Observable<Review | null>;
  edit = false;
  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.id = this.route.snapshot.paramMap.get('id')!;
    this.review$ = this.bookDetailsService.getLoggedReview(this.id);

    this.form = this.fb.group({
      rating: [0, [Validators.min(0), Validators.max(5)]],
      review: ['', [Validators.minLength(1), Validators.maxLength(500)]]
    });
  }

  ngOnInit(): void {
    this.review$.pipe(
        tap(review => {
          if (review){
            this.form.get('rating')?.setValue(review.rating!/2);
            this.form.get('review')?.setValue(review.review);

          }
        })
    ).subscribe()
  }

  onSubmit(){
    if (this.form.valid){
      this.bookDetailsService.setReview(this.id, this.form).pipe(
          map(() => {
            this.router.navigate([`/book/${this.id}`])
          })
      ).subscribe();
    }
  }


}
