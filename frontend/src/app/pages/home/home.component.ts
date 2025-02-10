import { Component } from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {TutorialCardsComponent} from "./components/tutorial-cards/tutorial-cards.component";
import {GenreButtonComponent} from "./components/genre-button/genre-button.component";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {GenreListComponent} from "./components/genre-list/genre-list.component";

@Component({
  selector: 'app-home',
	imports: [MatGridListModule, TutorialCardsComponent, GenreButtonComponent, GenreListComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

	genres: BookGenre[] = Array(18).fill(BookGenre.SCIENCE_FICTION);

	protected readonly BookGenre = BookGenre;
}
