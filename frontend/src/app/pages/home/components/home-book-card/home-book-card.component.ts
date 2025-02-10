import {Component, computed, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {DecimalPipe} from "@angular/common";
import {MatRipple} from "@angular/material/core";
import {RouterLink} from "@angular/router";
import {BookWithInfo} from "../../../../shared/model/book/bookWithInfo";
import {SalesCategory} from "../../../../shared/model/book/salesCategory";

@Component({
  selector: 'app-home-book-card',
	imports: [
		MatCard,
		DecimalPipe,
		MatCardContent,
		MatCardHeader,
		MatCardImage,
		MatRipple,
		RouterLink
	],
  templateUrl: './home-book-card.component.html',
  styleUrl: './home-book-card.component.scss'
})
export class HomeBookCardComponent {
	book = input.required<BookWithInfo>();

	protected readonly SalesCategory = SalesCategory;

	percentage = computed<number>(() => {
		if (this.book().deal == null){
			return 0;
		}
		const price = this.book().book.price || 0;
		const dealPrice = this.book().deal?.price || 0;
		return ((price-dealPrice)/price)*100;
	});
}
