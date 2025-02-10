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

	cols = this.updateColumns(window.innerWidth);

	@HostListener('window:resize', ['$event'])
	onResize(event: any) {
		this.cols = this.updateColumns(window.innerWidth);
	}

	updateColumns(width: number): number {
		if (width > 1500) {
			return 9;
		} else if (width > 1000) {
			return 6;
		} else if (width > 700) {
			return 4;
		} else {
			return 0;
		}
	}
}
