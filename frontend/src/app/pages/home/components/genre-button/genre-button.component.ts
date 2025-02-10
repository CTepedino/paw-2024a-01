import {Component, input} from '@angular/core';
import {RouterLink} from "@angular/router";
import {BookGenre} from "../../../../shared/model/book/bookGenre";
import {GenreIcon} from "../../../../shared/const/genreIcon";
import {MatIcon} from "@angular/material/icon";
import {MatFabAnchor} from "@angular/material/button";

@Component({
  selector: 'app-genre-button',
  imports: [
    RouterLink,
    MatIcon,
    MatFabAnchor
  ],
  templateUrl: './genre-button.component.html',
  styleUrl: './genre-button.component.scss'
})
export class GenreButtonComponent {
  link = input.required<string>()
  genre = input.required<BookGenre>()

  protected readonly GenreIcon = GenreIcon;
}
