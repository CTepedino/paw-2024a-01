import {Component} from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {TutorialCardsComponent} from "./components/tutorial-cards/tutorial-cards.component";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {GenreListComponent} from "./components/genre-list/genre-list.component";
import {BookCardComponent} from "../../shared/components/book-card/book-card.component";
import {SalesCategory} from "../../shared/model/book/salesCategory";
import {HomeBookCardComponent} from "./components/home-book-card/home-book-card.component";

@Component({
  selector: 'app-home',
	imports: [MatGridListModule, TutorialCardsComponent, GenreListComponent, BookCardComponent, HomeBookCardComponent],
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
		genre: BookGenre.FICTION,
		salesCategory: SalesCategory.BEST_SELLER,
		cover: "assets/book-cover.jpg"
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
	bestSellers = this.books.slice(0, 6);
	newDeals = this.books.slice(0, 3);
}
