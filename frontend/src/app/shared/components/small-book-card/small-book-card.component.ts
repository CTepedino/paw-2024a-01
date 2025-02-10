import {booleanAttribute, Component, computed, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {DatePipe, DecimalPipe} from "@angular/common";
import {MatRipple} from "@angular/material/core";
import {RouterLink} from "@angular/router";
import {BookWithInfo} from "../../model/book/bookWithInfo";
import {SalesCategory} from "../../model/book/salesCategory";

@Component({
  selector: 'app-small-book-card',
	imports: [
		MatCard,
		DecimalPipe,
		MatCardContent,
		MatCardHeader,
		MatCardImage,
		MatRipple,
		RouterLink,
		DatePipe
	],
  templateUrl: './small-book-card.component.html',
  styleUrl: './small-book-card.component.scss'
})
export class SmallBookCardComponent {
	book = input.required<BookWithInfo>();
	showDate = input(false, {transform: booleanAttribute});

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
