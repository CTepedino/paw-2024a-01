import {Component, inject, OnInit} from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";

import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {MatFormField, MatLabel} from "@angular/material/form-field";
import {MatInput} from "@angular/material/input";
import {MatOption} from "@angular/material/core";
import {MatSelect} from "@angular/material/select";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {BookCardComponent} from "../../shared/components/book-card/book-card.component";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";
import {BookWithDataService} from "../../shared/services/book-with-data.service";
import {BookGenre} from "../../shared/model/book/bookGenre";
import {BookSearchOrderBy} from "../../shared/model/book/bookSearchOrderBy";
import {catchError, map, Observable, of} from "rxjs";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {BookWithData} from "../../shared/model/book/bookWithData";
import {ActivatedRoute, Router} from "@angular/router";
import {PaginatorComponent} from "../../shared/components/paginator/paginator.component";
import {NgxPaginationModule} from "ngx-pagination";
import {Title} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";


@Component({
  selector: 'app-search2',
  imports: [
    MatGridList,
    MatGridTile,
    ActionButtonComponent,
    FormsModule,
    MatFormField,
    MatInput,
    MatLabel,
    MatOption,
    MatSelect,
    ReactiveFormsModule,
    BookCardComponent,
    AsyncPipe,
    PaginatorComponent,
    JsonPipe,
    NgxPaginationModule,
    TranslateModule,
  ],
  templateUrl: './search.component.html',
  styleUrl: './search.component.scss'
})
export class SearchComponent implements OnInit {
  title = inject(Title);
  route = inject(ActivatedRoute);
  router = inject(Router);
  bookWithDataService = inject(BookWithDataService);

  pagination$!: Observable<PaginatedContent<BookWithData>>;
  books$!: Observable<BookWithData[]>
  currentPage!: number;
  pageSize = 20;

  form: FormGroup;

  constructor(private fb: FormBuilder) {
    this.title.setTitle('Search')

    this.form = this.fb.group({
      title: [''],
      orderBy: [BookSearchOrderBy.PUBLICATION_DATE_DESC],
      genre: [null],
      minPages: [null],
      maxPages: [null],
      minPrice: [null],
      maxPrice: [null],
      minAge: [null],
      maxAge: [null]
    });
  }

    ngOnInit() {
        this.route.queryParams.subscribe(params => {
          this.currentPage = Number(params['page']) || 1;
          this.form.get('title')?.setValue(params['title']);
          if (Object.values(BookSearchOrderBy).includes(params['order_by'])){
            this.form.get('orderBy')?.setValue(params['order_by']);
          }
          if (Object.values(BookGenre).includes(params['genre'])){
            this.form.get('genre')?.setValue(params['genre']);
          }
          this.form.get('minPages')?.setValue(params['min_pages']);
          this.form.get('maxPages')?.setValue(params['max_pages']);
          this.form.get('minPrice')?.setValue(params['min_price']);
          this.form.get('maxPrice')?.setValue(params['max_price']);
          this.form.get('minAge')?.setValue(params['min_age']);
          this.form.get('maxAge')?.setValue(params['max_age']);

          this.form.updateValueAndValidity();
        });
        this.fetchBooks();
    }

    onSubmit(){
      this.currentPage = 1;
      this.fetchBooks();
    }

    resetFilters(){
      this.form.get('title')?.setValue('');
      this.form.get('genre')?.setValue(null);
      this.form.get('orderBy')?.setValue(BookSearchOrderBy.PUBLICATION_DATE_DESC)
      this.form.get('minPages')?.setValue(null)
      this.form.get('maxPages')?.setValue(null)
      this.form.get('minPrice')?.setValue(null)
      this.form.get('maxPrice')?.setValue(null)
      this.form.get('minAge')?.setValue(null)
      this.form.get('maxAge')?.setValue(null)
      this.form.updateValueAndValidity();
      this.onSubmit();
    }

    onPageChange(page: number){
      this.currentPage = page;
      this.fetchBooks();
    }

    fetchBooks(){
      this.pagination$ = this.bookWithDataService.listBooksWithData({
        page: this.currentPage,
        size: this.pageSize,
        title: this.form.get('title')?.value,
        genre: this.form.get('genre')?.value,
        order_by: this.form.get('orderBy')?.value,
        min_page_count: this.form.get('minPages')?.value,
        max_page_count: this.form.get('maxPages')?.value,
        min_price: this.form.get('minPrice')?.value,
        max_price: this.form.get('maxPrice')?.value,
        min_suggested_age: this.form.get('minAge')?.value,
        max_suggested_age: this.form.get('maxAge')?.value,
      });

      this.books$ = this.pagination$.pipe(
          map((page) => page.data),
      );

      this.router.navigate([], {
        relativeTo: this.route,
        queryParams: {
          page: this.currentPage,
          title: this.form.get('title')?.value,
          genre: this.form.get('genre')?.value,
          order_by: this.form.get('orderBy')?.value,
          min_pages: this.form.get('minPages')?.value,
          max_pages: this.form.get('maxPages')?.value,
          min_price: this.form.get('minPrice')?.value,
          max_price: this.form.get('maxPrice')?.value,
          min_age: this.form.get('minAge')?.value,
          max_age: this.form.get('maxAge')?.value,
        },
        queryParamsHandling: 'merge',
      });
    }

  protected readonly BookGenre = BookGenre;
  protected readonly Object = Object;
  protected readonly BookSearchOrderBy = BookSearchOrderBy;

}
