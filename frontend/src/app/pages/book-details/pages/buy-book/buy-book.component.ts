import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs";
import {BookWithData} from "../../../../shared/model/book/bookWithData";
import {BookDetailsService} from "../../store/book-details.service";
import {AsyncPipe} from "@angular/common";
import {BuyBookFormComponent} from "./components/buy-book-form/buy-book-form.component";
import {BuySucessComponent} from "./components/buy-sucess/buy-sucess.component";
import {Title} from "@angular/platform-browser";
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-buy-book',
	imports: [
		AsyncPipe,
		BuyBookFormComponent,
		BuySucessComponent,
	],
  templateUrl: './buy-book.component.html',
  styleUrl: './buy-book.component.scss'
})
export class BuyBookComponent implements OnInit {
  private bookDetailsService = inject(BookDetailsService);
  private route = inject(ActivatedRoute);
  private title = inject(Title);

  id: number | null = null;
  book$: Observable<BookWithData> | undefined;

  buying = true;

  constructor(private translate: TranslateService) {
	  this.translate.get('BUY_BOOK_BROWSER').subscribe(translatedTitle => {
		  this.title.setTitle(translatedTitle);
	  });
  }

  ngOnInit() {
    this.id = Number(this.route.snapshot.paramMap.get('id'));
    this.book$ = this.bookDetailsService.getBook(this.id!);
  }

  bought(){
    this.buying = false;
  }
}
