import {Component, inject, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatSelectModule} from '@angular/material/select';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MatButtonModule} from '@angular/material/button';
import {FormBuilder, FormGroup, ReactiveFormsModule} from '@angular/forms';
import {PurchasesOrderCardComponent} from "./components/purchases-order-card/purchases-order-card.component";
import {NgxPaginationModule} from "ngx-pagination";
import {PaginatorComponent} from "../../shared/components/paginator/paginator.component";
import {OrderStatus} from "../../shared/model/order/orderStatus";
import {OrderWithDataService} from "../../shared/services/order-with-data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {OrderWithData} from "../../shared/model/order/orderWithData";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";
import {BookSearchOrderBy} from "../../shared/model/book/bookSearchOrderBy";

@Component({
  selector: 'app-purchases',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatGridListModule,
    MatCheckboxModule,
    MatButtonModule,
    PurchasesOrderCardComponent,
    NgxPaginationModule,
    PaginatorComponent,
    ReactiveFormsModule,
    TranslateModule
  ],
  templateUrl: './purchases.component.html',
  styleUrl: './purchases.component.scss'
})
export class PurchasesComponent implements OnInit {
  title = inject(Title);
  ordersWithDataService = inject(OrderWithDataService);
  private route = inject(ActivatedRoute);
  private router = inject(Router)

  pagination$!: Observable<PaginatedContent<OrderWithData>>;
  orders$!: Observable<OrderWithData[]>
  currentPage!: number;
  pageSize = 20;

  form: FormGroup;

  constructor(private fb: FormBuilder, private translate: TranslateService) {
    this.translate.get('PURCHASES_BROWSER').subscribe(translatedTitle => {
      this.title.setTitle(translatedTitle);
    });


    this.form = fb.group({
      title: [''],
      status: [null]
    })
  }

  ngOnInit(): void{
    this.route.queryParams.subscribe(params => {
      this.currentPage =Number(params['page']) || 1;
      this.form.get('title')?.setValue(params['title']);
      if (Object.values(OrderStatus).includes(params['status'])){
        this.form.get('status')?.setValue(params['status']);
      }
      this.form.updateValueAndValidity();
    });
    this.fetchOrders();
  }

  onPageChange(page: number){
    this.currentPage = page;
    this.fetchOrders();
  }

  onSubmit(){
    this.currentPage = 1;
    this.fetchOrders();
  }

  fetchOrders(){
    this.pagination$ = this.ordersWithDataService.getPurchases({
      page: this.currentPage,
      size: this.pageSize,
      title: this.form.get('title')?.value,
      status: this.form.get('status')?.value
    });

    this.orders$ = this.pagination$.pipe(
        map((page) => page.data)
    );

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: this.currentPage,
        title: this.form.get('title')?.value,
        status: this.form.get('status')?.value
      },
      queryParamsHandling: 'merge',
    });
  }

  protected readonly OrderStatus = OrderStatus;
  protected readonly BookSearchOrderBy = BookSearchOrderBy;
  protected readonly Object = Object;
}