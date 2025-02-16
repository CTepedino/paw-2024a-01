import {Component, inject, input, OnInit} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {NgxPaginationModule} from "ngx-pagination";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {SmallBookCardComponent} from "../../../../shared/components/small-book-card/small-book-card.component";
import {ActivatedRoute, Router} from "@angular/router";
import {UserProfileService} from "../../store/user-profile.service";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {BookWithData} from "../../../../shared/model/book/bookWithData";
import {FormBuilder, FormGroup, ReactiveFormsModule} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatOption, MatSelect} from "@angular/material/select";
import {BookSearchOrderBy, BookSearchOrderByOptions} from "../../../../shared/model/book/bookSearchOrderBy";
import {MatInput} from "@angular/material/input";

@Component({
  selector: 'app-publications-tab',
	imports: [
		AsyncPipe,
		MatGridList,
		MatGridTile,
		NgxPaginationModule,
		PaginatorComponent,
		SmallBookCardComponent,
		ReactiveFormsModule,
		MatFormField,
		MatSelect,
		MatOption,
		MatLabel,
		MatInput
	],
  templateUrl: './publications-tab.component.html',
  styleUrl: './publications-tab.component.scss'
})
export class PublicationsTabComponent implements OnInit {
	private route = inject(ActivatedRoute);
	private router = inject(Router);
	private profileService = inject(UserProfileService);

	userId = input.required<number>();

	pagination$!: Observable<PaginatedContent<BookWithData>>;
	books$!: Observable<BookWithData[]>
	currentPage!: number;
	pageSize = 20;

	form: FormGroup;

	constructor(private fb: FormBuilder) {
		this.form = fb.group({
			title: [''],
			orderBy: [BookSearchOrderBy.PUBLICATION_DATE_DESC]
		})
	}

	ngOnInit() {
		this.route.queryParams.subscribe(params => {
			this.currentPage = Number(params['page']) || 1;
			this.form.get('title')?.setValue(params['title']);
			if (Object.values(BookSearchOrderBy).includes(params['order_by'])){
				this.form.get('orderBy')?.setValue(params['order_by']);
			}
			this.form.updateValueAndValidity();
		});

		this.pagination$ =  this.profileService.getPublications({
			page: this.currentPage,
			size: this.pageSize,
			title: this.form.get('title')?.value,
			order_by: this.form.get('orderBy')?.value
		}, this.userId())
		this.books$ = this.pagination$.pipe(
			map((page) => page.data)
		)
	}

	onPageChange(page: number){
		this.router.navigate([], {
			relativeTo: this.route,
			queryParams: { page: page },
			queryParamsHandling: 'merge',
		});

		this.currentPage = page;

		this.books$ = this.profileService.getPublications({
			page: this.currentPage,
			size: this.pageSize,
			title: this.form.get('title')?.value,
			order_by: this.form.get('orderBy')?.value
		}, this.userId()).pipe(
			map((page) => page.data)
		)
	}

	onSubmit(){
		this.currentPage = 1;
		this.pagination$ = this.profileService.getPublications({
			page: this.currentPage,
			size: this.pageSize,
			title: this.form.get('title')?.value,
			order_by: this.form.get('orderBy')?.value
		}, this.userId())

		this.router.navigate([], {
			relativeTo: this.route,
			queryParams: {
				page: 1,
				title: this.form.get('title')?.value,
				order_by: this.form.get('orderBy')?.value
			},
			queryParamsHandling: 'merge',
		});

		this.books$ = this.pagination$.pipe(
			map((page) => page.data)
		)
	}


	protected readonly BookSearchOrderBy = BookSearchOrderBy;
	protected readonly BookSearchOrderByOptions = BookSearchOrderByOptions;
}
