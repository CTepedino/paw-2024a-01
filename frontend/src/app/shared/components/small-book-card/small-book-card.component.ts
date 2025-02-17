import {booleanAttribute, Component, computed, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {DecimalPipe, NgOptimizedImage} from "@angular/common";
import {MatRipple} from "@angular/material/core";
import {RouterLink} from "@angular/router";
import {BookWithData} from "../../model/book/bookWithData";
import {SalesCategory} from "../../model/book/salesCategory";
import {BookBadgeComponent} from "../book-badge/book-badge.component";

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
		BookBadgeComponent,
		NgOptimizedImage,
	],
  templateUrl: './small-book-card.component.html',
  styleUrl: './small-book-card.component.scss'
})
export class SmallBookCardComponent {
	book = input.required<BookWithData>();
	showDate = input(false, {transform: booleanAttribute});
	showWriter = input(true, {transform: booleanAttribute});

	protected readonly SalesCategory = SalesCategory;

	percentage = computed<number>(() => {
		if (this.book().deal == null){
			return 0;
		}
		const price = this.book().price || 0;
		const dealPrice = this.book().dealInfo?.price || 0;
		return ((price-dealPrice)/price)*100;
	});
}
