import {Component, computed, input} from '@angular/core';
import {Book} from "../../model/book/book";
import {SalesCategory} from "../../model/book/salesCategory";
import {User} from "../../model/user/user";
import {Deal} from "../../model/book/deal";
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {DatePipe, DecimalPipe} from "@angular/common";
import {MatRipple} from "@angular/material/core";
import {BookWithInfo} from "../../model/book/bookWithInfo";
import {MatBadge} from "@angular/material/badge";

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
		MatCardHeader,
		MatRipple,
		MatBadge
	],
  templateUrl: './book-card.component.html',
  styleUrl: './book-card.component.scss'
})
export class BookCardComponent {
	book = input.required<BookWithInfo>();

    protected readonly SalesCategory = SalesCategory;

	percentage = computed<number>(() => {
		if (this.book().deal == null){
			return 0;
		}
		const price = this.book().book.price || 0;
		const dealPrice = this.book().deal?.price || 0;
		return ((price-dealPrice)/price)*100;
	} )
}
