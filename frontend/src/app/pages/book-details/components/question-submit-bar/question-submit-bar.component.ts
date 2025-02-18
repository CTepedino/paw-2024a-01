import {Component, inject, input, output} from '@angular/core';
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel, MatPrefix} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {concatMap} from "rxjs";
import {QuestionService} from "../../../../shared/services/question.service";
import {AuthService} from "../../../../shared/services/auth.service";

@Component({
  selector: 'app-question-submit-bar',
  imports: [
    ActionButtonComponent,
    FormsModule,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    MatPrefix
  ],
  templateUrl: './question-submit-bar.component.html',
  styleUrl: './question-submit-bar.component.scss'
})
export class QuestionSubmitBarComponent {
  questionService = inject(QuestionService);

  bookId = input.required<number>();

  submitted = output<void>();

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = this.fb.group({
      question: ['', [Validators.required, Validators.maxLength(500)]]
    })
  }

  onSubmit(){
    if (this.form.valid){
      this.questionService.postQuestion({question: this.form.get('question')?.value, bookId: this.bookId()}).subscribe(() => {
        this.submitted.emit();
        this.form.reset();
      });
    }
  }

}
