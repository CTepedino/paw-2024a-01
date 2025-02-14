import {booleanAttribute, Component, inject, input, OnInit, signal} from '@angular/core';
import {QuestionWithData} from "../../../../shared/model/question/questionWithData";
import {DatePipe, NgOptimizedImage} from "@angular/common";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {QuestionService} from "../../../../shared/services/question.service";
import {tap} from "rxjs";

@Component({
  selector: 'app-question-card',
  imports: [
    NgOptimizedImage,
    RouterLink,
    MatIcon,
    DatePipe,
    MatFormField,
    FormsModule,
    MatInput,
    MatLabel,
    ReactiveFormsModule,
    ActionButtonComponent
  ],
  templateUrl: './question-card.component.html',
  styleUrl: './question-card.component.scss'
})
export class QuestionCardComponent implements OnInit{
  questionService = inject(QuestionService);

  received = input(false, {transform: booleanAttribute})
  question = input.required<QuestionWithData>();

  showAnswer = signal(true);

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.form = fb.group({
      answer: ['', [Validators.required, Validators.maxLength(500)]]
    })
  }

  ngOnInit(): void {
    this.showAnswer.set(!!this.question().answerInfo)
  }



  onSubmit(){
    if (this.form.valid){
      this.questionService.putAnswer(this.question().self!, {answer: this.form.get('answer')?.value}).pipe(
          tap(() => {
            this.question().answerInfo ={ answer: this.form.get('answer')?.value, answerDate: new Date().toString()};
            this.showAnswer.set(true);
          })
      ).subscribe();
    }
  }
}
