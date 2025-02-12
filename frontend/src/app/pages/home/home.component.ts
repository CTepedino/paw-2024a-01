import {Component, inject, OnInit} from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {TutorialCardsComponent} from "./components/tutorial-cards/tutorial-cards.component";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {GenreListComponent} from "./components/genre-list/genre-list.component";
import {BookCardComponent} from "../../shared/components/book-card/book-card.component";
import {SmallBookCardComponent} from "../../shared/components/small-book-card/small-book-card.component";
import {HomeService} from "./store/home.service";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {AsyncPipe} from "@angular/common";
import {ActivatedRoute, Router} from "@angular/router";
import {BookWithInfo} from "../../shared/model/book/bookWithInfo";
import {NgxPaginationModule, PaginationInstance} from "ngx-pagination";
import {PaginatorComponent} from "../../shared/components/paginator/paginator.component";

@Component({
  selector: 'app-home',
	imports: [MatGridListModule, TutorialCardsComponent, GenreListComponent, BookCardComponent, SmallBookCardComponent, AsyncPipe, NgxPaginationModule, PaginatorComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent implements OnInit {
	private homeService = inject(HomeService);
	private route = inject(ActivatedRoute);
	private router = inject(Router)


	pagination$!: Observable<PaginatedContent<BookWithInfo>>;
	books$!: Observable<BookWithInfo[]>
	currentPage!: number;
	pageSize = 20;

	ngOnInit(): void{
		this.route.queryParams.subscribe(params => {
			this.currentPage =Number(params['page']) || 1;
		});
		this.pagination$ =  this.homeService.getRecentBooks(this.currentPage, this.pageSize)
		this.books$ = this.pagination$.pipe(
			map((page) => page.data)
		)
	}


	genres$ = this.homeService.getPopularGenres(12);
	bestSellers$ = this.homeService.getBestSellers(6);
	newDeals$ = this.homeService.getNewDeals(6);




	onPageChange(page: number){

		this.router.navigate([], {
			relativeTo: this.route,
			queryParams: { page: page },
			queryParamsHandling: 'merge',
		});

		//this.currentPage.set(page);

		this.books$ = this.homeService.getRecentBooks(page, this.pageSize).pipe(
			map((page) => page.data)
		)
	}

	protected readonly BookGenre = BookGenre;

}
