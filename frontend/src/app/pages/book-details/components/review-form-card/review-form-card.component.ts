import {Component, Inject} from '@angular/core';
import {NgbRating} from "@ng-bootstrap/ng-bootstrap";
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from "@angular/forms";
import {Review} from "../../../../shared/model/review/review";
import {MatFormField, MatHint, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {CancelButtonComponent} from "../../../../shared/components/cancel-button/cancel-button.component";
import {
  MAT_DIALOG_DATA,
  MatDialogActions,
  MatDialogContent,
  MatDialogRef,
  MatDialogTitle
} from "@angular/material/dialog";

@Component({
  selector: 'app-review-form-card',
  imports: [
    NgbRating,
    MatFormField,
    MatHint,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    ActionButtonComponent,
    CancelButtonComponent,
    MatDialogTitle,
    MatDialogContent,
    MatDialogActions,


  ],
  templateUrl: './review-form-card.component.html',
  styleUrl: './review-form-card.component.scss'
})
export class ReviewFormCardComponent {
  rating = 0;
  review = '';

  edit = false;
  form: FormGroup;

  constructor(public dialogRef: MatDialogRef<ReviewFormCardComponent>,
              @Inject(MAT_DIALOG_DATA) public data: { review: Review | null },
              private fb: FormBuilder
  ){
    if (data.review){
      this.edit = true;
      this.rating = data.review?.rating!/2;
      this.review = data.review?.review!;
    }

    this.form = this.fb.group({
      rating: [this.rating, [Validators.min(0), Validators.max(5)]],
      review: [this.review, [Validators.minLength(1), Validators.maxLength(500)]]
    });
  }

  cancelReview(){
    this.dialogRef.close({update: false});
  }


  onSubmit(){
    if (this.form.valid){
      this.dialogRef.close({
        update: true,
        review: {
          rating: this.form.get('rating')?.value * 2,
          review: this.form.get('review')?.value
        }
      })
    }
  }


}
