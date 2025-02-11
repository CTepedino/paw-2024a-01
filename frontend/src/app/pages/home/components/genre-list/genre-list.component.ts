import {Component, HostListener, input} from '@angular/core';
import {GenreButtonComponent} from "../genre-button/genre-button.component";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {BookGenre} from "../../../../shared/model/book/bookGenre";

@Component({
  selector: 'app-genre-list',
	imports: [
		GenreButtonComponent,
		MatGridList,
		MatGridTile
	],
  templateUrl: './genre-list.component.html',
  styleUrl: './genre-list.component.scss'
})
export class GenreListComponent {
	genres = input.required<BookGenre[]>();

	protected readonly BookGenre = BookGenre;

}
