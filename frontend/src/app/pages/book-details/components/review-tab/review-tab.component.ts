import {Component, inject, input, OnInit} from '@angular/core';
import {OrderStatus} from "../../../../shared/model/order/orderStatus";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormField} from "@angular/material/form-field";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {ReviewOrderBy} from "../../../../shared/model/review/reviewOrderBy";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {ReviewWithInfo} from "../../../../shared/model/review/reviewWithInfo";
import {BookDetailsService} from "../../store/book-details.service";
import {ReviewCardComponent} from "../review-card/review-card.component";
import {ActivatedRoute, Router} from "@angular/router";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {AsyncPipe} from "@angular/common";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {NgxPaginationModule} from "ngx-pagination";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-review-tab',
	imports: [
		FormsModule,
		MatFormField,
		MatOption,
		MatSelect,
		ReactiveFormsModule,
		ReviewCardComponent,
		MatGridList,
		AsyncPipe,
		MatGridTile,
		PaginatorComponent,
		NgxPaginationModule,
		TranslateModule
	],
  templateUrl: './review-tab.component.html',
  styleUrl: './review-tab.component.scss'
})
export class ReviewTabComponent implements OnInit {
	bookDetailsService = inject(BookDetailsService);
	route = inject(ActivatedRoute);
	router = inject(Router);

	loggedUserReview = input<ReviewWithInfo | null>(null);
	bookId = input.required<number>();

	pagination$!: Observable<PaginatedContent<ReviewWithInfo>>;
	reviews$!: Observable<ReviewWithInfo[]>
	currentPage!: number;
	pageSize = 20;

	form: FormGroup;

	constructor(private fb: FormBuilder) {
		this.form = this.fb.group({
			orderBy: [ReviewOrderBy.DATE_DESC]
		})
	}

	ngOnInit(): void{
		this.route.queryParams.subscribe(params => {
			this.currentPage = Number(params['page']) || 1;
			if (Object.values(OrderStatus).includes(params['order_by'])){
				this.form.get('orderBy')?.setValue(params['order_by']);
			}
			this.form.updateValueAndValidity();
		});
		this.fetchReviews(true);
	}

	onPageChange(page: number){
		this.currentPage = page;
		this.fetchReviews();
	}

	onSubmit(){
		this.currentPage = 1;
		this.fetchReviews();
	}

	fetchReviews(keepUrl: boolean = false){
		this.pagination$ = this.bookDetailsService.getReviews(this.bookId(), {
			order_by: this.form.get('orderBy')?.value,
			page: this.currentPage,
			size: this.pageSize
		});

		this.reviews$ = this.pagination$.pipe(
			map((page) => page.data)
		);

		if (!keepUrl){
			this.router.navigate([], {
				relativeTo: this.route,
				queryParams: {
					page: this.currentPage,
					status: this.form.get('orderBy')?.value
				},
				queryParamsHandling: 'merge',
			});
		}

	}

	protected readonly Object = Object;
	protected readonly ReviewOrderBy = ReviewOrderBy;
}
