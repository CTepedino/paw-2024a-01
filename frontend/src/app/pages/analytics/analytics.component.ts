import {Component, inject, OnInit} from '@angular/core';
import {WriterCategory} from "../../shared/model/user/writerCategory";
import {MatGridListModule} from "@angular/material/grid-list";
import {AsyncPipe, CurrencyPipe} from "@angular/common";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {Title} from "@angular/platform-browser";
import {AnalyticsCardComponent} from "./components/analytics-card/analytics-card.component";
import {Analytics, AnalyticsService, BookWithAnalytics} from "./store/analytics.service";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {PaginatorComponent} from "../../shared/components/paginator/paginator.component";
import {ActivatedRoute, Router} from "@angular/router";
import {User} from "../../shared/model/user/user";
import {AuthService} from "../../shared/services/auth.service";
import {NgxPaginationModule} from "ngx-pagination";

@Component({
  selector: 'app-analytics',
  imports: [MatGridListModule, CurrencyPipe, MatCheckboxModule, FormsModule, MatFormFieldModule, MatSelectModule, AnalyticsCardComponent, AsyncPipe, PaginatorComponent, ReactiveFormsModule, NgxPaginationModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent implements OnInit {
  title = inject(Title);
  analyticsService = inject(AnalyticsService);
  authService = inject(AuthService);
  private route = inject(ActivatedRoute);
  private router = inject(Router);

  user: User | null = null;
  total: Analytics | null = null;
  currentMonthTotal: Analytics | null = null;
  monthlyTotal$: Observable<Analytics> | null = null;

  pagination$!: Observable<PaginatedContent<BookWithAnalytics>>;
  books$!: Observable<BookWithAnalytics[]>;
  currentPage!: number;
  pageSize = 20;

  showByMonth = false;

  startYear = 2024;
  years = Array.from({length: new Date().getFullYear() - this.startYear + 1}, (_, i) => this.startYear + i);
  months = [
    { name: "January", number: 1 },
    { name: "February", number: 2 },
    { name: "March", number: 3 },
    { name: "April", number: 4 },
    { name: "May", number: 5 },
    { name: "June", number: 6 },
    { name: "July", number: 7 },
    { name: "August", number: 8 },
    { name: "September", number: 9 },
    { name: "October", number: 10 },
    { name: "November", number: 11 },
    { name: "December", number: 12 }
  ];

  form: FormGroup;

  constructor(private fb: FormBuilder){
    this.title.setTitle('Analytics');
    this.form = fb.group({
      year: [new Date().getFullYear()],
      month: [new Date().getMonth()]
    })

    this.authService.getLoggedUser().subscribe(user => this.user = user);
    this.analyticsService.getTotal().subscribe((analytics) => this.total = analytics);
    this.analyticsService.getCurrentMonthTotal().subscribe((analytics) => this.currentMonthTotal = analytics);
  }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.currentPage = Number(params['page']) || 1;
      this.showByMonth = params['show_by_month'] != 'false';
      if (params['year'] > this.startYear){
        this.form.get('year')?.setValue(Number(params['year']));
      }
      if (params['month'] >= 1 && params['month'] <= 12){
        this.form.get('month')?.setValue(Number(params['month']));
      }
      this.form.updateValueAndValidity();
    });

    this.fetchBooks();
  }

  fetchBooks(){
    this.monthlyTotal$ = this.analyticsService.getMonthlyTotal(this.form.get('year')?.value, this.form.get('month')?.value);

    if (this.showByMonth){
      this.pagination$ = this.analyticsService.getBooksWithMonthlyAnalytics(
          this.form.get('year')?.value,
          this.form.get('month')?.value,
          this.currentPage,
          this.pageSize
      )
    } else {
      this.pagination$ = this.analyticsService.getBooksWithAnalytics(this.currentPage, this.pageSize);
    }
    this.books$ = this.pagination$.pipe(
        map((pagination) => pagination.data)
    );


    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: this.currentPage,
        show_by_month: this.showByMonth,
        year: this.form.get('year')?.value,
        month: this.form.get('month')?.value
      },
      queryParamsHandling: 'merge',
    });
  }

  setBooks(event: any){
    this.showByMonth = event.checked;
    this.currentPage = 1;
    this.fetchBooks();
  }

  onPageChange(page: number){
    this.currentPage = page;
    this.fetchBooks();
  }

  onSubmit(){
    this.currentPage = 1;
    this.fetchBooks();
  }

  protected readonly WriterCategory = WriterCategory;
}
