import {Component, computed, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {RouterLink} from "@angular/router";
import {MatIcon} from "@angular/material/icon";
import {DatePipe, DecimalPipe, NgOptimizedImage} from "@angular/common";
import {MatRipple} from "@angular/material/core";
import {BookBadgeComponent} from "../../../../shared/components/book-badge/book-badge.component";
import {BookWithInfo} from "../../../../shared/model/book/bookWithInfo";


@Component({
	selector: 'app-book-card-search',
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
		BookBadgeComponent,
		NgOptimizedImage
	],
	templateUrl: './book-card-serach.component.html',
	styleUrl: './book-card-serach.component.scss'
})
export class BookCardSerachComponent {
	book = {
		cover: "assets/book-cover.jpg",
		title: "Test book tengo un nombre largo",
		price: 300,
		author: "Juan Lopez tengo un nombre largo",
		genre: "Fiction",
		age: 15,
		pages: 200,
		year: 2024,
		percentage: 20,
		deal: 200
	}

	books = Array(10).fill(this.book);
	//orders = Array()
}
