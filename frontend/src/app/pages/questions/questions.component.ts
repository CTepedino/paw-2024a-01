import { Component } from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {QuestionCardComponent} from "./components/question-card/question-card.component";

@Component({
  selector: 'app-questions',
	imports: [
		RouterOutlet,
		QuestionCardComponent
	],
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.scss'
})
export class QuestionsComponent {

}
