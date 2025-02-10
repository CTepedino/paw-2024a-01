import { Component } from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {TutorialCardsComponent} from "./components/tutorial-cards/tutorial-cards.component";
import {GenreButtonComponent} from "./components/genre-button/genre-button.component";
import {BookGenre} from "../../shared/model/book/bookGenre";

@Component({
  selector: 'app-home',
	imports: [MatGridListModule, TutorialCardsComponent, GenreButtonComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

	protected readonly BookGenre = BookGenre;
}
