import { Component } from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {TutorialCardsComponent} from "./components/tutorial-cards/tutorial-cards.component";
import {GenreButtonComponent} from "./components/genre-button/genre-button.component";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {GenreListComponent} from "./components/genre-list/genre-list.component";
import {BookCardComponent} from "../../shared/components/book-card/book-card.component";
import {MatRow} from "@angular/material/table";

@Component({
  selector: 'app-home',
	imports: [MatGridListModule, TutorialCardsComponent, GenreButtonComponent, GenreListComponent, BookCardComponent, MatRow],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

	genres: BookGenre[] = Array(18).fill(BookGenre.SCIENCE_FICTION);

	protected readonly BookGenre = BookGenre;

	book = {
		id: 1,
		title: "my book",
		averageRating: 2,
		suggestedAge: 12,
		pageCount: 102,
		publishDate: '2023-05-05',
		price: 40000,
		genre: BookGenre.FICTION
	}
	user = {
		firstName: 'juan',
		lastName: 'perez'
	}
	deal = {
		price: 15000
	}

	bookWithInfo = {book: this.book, writer: this.user, deal: this.deal}

	books = Array(10).fill(this.bookWithInfo);
}
