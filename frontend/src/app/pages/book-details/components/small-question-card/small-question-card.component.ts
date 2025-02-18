import {booleanAttribute, Component, inject, input, signal} from '@angular/core';
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {DatePipe, NgOptimizedImage} from "@angular/common";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatIcon} from "@angular/material/icon";
import {MatInput} from "@angular/material/input";
import {RouterLink} from "@angular/router";
import {TranslatePipe} from "@ngx-translate/core";
import {QuestionService} from "../../../../shared/services/question.service";
import {QuestionWithData} from "../../../../shared/model/question/questionWithData";
import {tap} from "rxjs";
import {Question} from "../../../../shared/model/question/question";

@Component({
  selector: 'app-small-question-card',
  imports: [
    ActionButtonComponent,
    DatePipe,
    FormsModule,
    MatFormField,
    MatIcon,
    MatInput,
    MatLabel,
    NgOptimizedImage,
    ReactiveFormsModule,
    RouterLink,
    TranslatePipe
  ],
  templateUrl: './small-question-card.component.html',
  styleUrl: './small-question-card.component.scss'
})
export class SmallQuestionCardComponent {
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
