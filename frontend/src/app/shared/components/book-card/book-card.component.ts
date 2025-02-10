import {Component, input} from '@angular/core';
import {Book} from "../../model/book/book";
import {SalesCategory} from "../../model/book/salesCategory";
import {User} from "../../model/user/user";
import {Deal} from "../../model/book/deal";
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {DatePipe, DecimalPipe} from "@angular/common";

@Component({
  selector: 'app-book-card',
  imports: [
    MatCard,
    RouterLink,
    MatCardImage,
    MatCardContent,
    MatIcon,
    DatePipe,
    DecimalPipe,
    MatCardHeader
  ],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
  book = input.required<Book>();
  writer = input.required<User>();
  deal = input<Deal | null>(null);

  protected readonly SalesCategory = SalesCategory;
}
