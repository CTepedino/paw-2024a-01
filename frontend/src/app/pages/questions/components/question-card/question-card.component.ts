import {booleanAttribute, Component, input} from '@angular/core';
import {Question} from "../../../../shared/model/question/question";
import {QuestionWithData} from "../../../../shared/model/question/questionWithData";
import {DatePipe, NgOptimizedImage} from "@angular/common";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatInput} from "@angular/material/input";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";

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
export class QuestionCardComponent {
  received = input(false, {transform: booleanAttribute})

  question: QuestionWithData = {
    bookInfo: {
      cover: 'assets/book-cover.jpg',
      title: 'Padel Knowledge'
    },
    writerInfo: {
      lastName: 'perez',
      firstName: 'martin'
    },
    questionerInfo: {
      lastName: 'perez2',
      firstName: 'martin2'
    },
    question: 'is it a book?',
    date: '2024-02-08'
  }
}
