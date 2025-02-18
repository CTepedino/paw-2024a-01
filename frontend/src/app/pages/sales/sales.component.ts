import {Component, inject, OnInit} from '@angular/core';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormBuilder, FormGroup, FormsModule, ReactiveFormsModule} from "@angular/forms";
import {OrderStatus} from "../../shared/model/order/orderStatus";
import {SalesOrderCardComponent} from "./components/sales-order-card/sales-order-card.component";
import {OrderWithDataService} from "../../shared/services/order-with-data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {OrderWithData} from "../../shared/model/order/orderWithData";
import {AsyncPipe} from "@angular/common";
import {NgxPaginationModule} from "ngx-pagination";
import {PaginatorComponent} from "../../shared/components/paginator/paginator.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-sales',
  imports: [MatGridListModule, MatFormFieldModule, MatLabel, MatInputModule, MatSelectModule, FormsModule, SalesOrderCardComponent, AsyncPipe, NgxPaginationModule, PaginatorComponent, ReactiveFormsModule, TranslateModule],
  templateUrl: './sales.component.html',
  styleUrl: './sales.component.scss',
})
export class SalesComponent implements OnInit{
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
    this.translate.get('SALES_BROWSER').subscribe(translatedTitle => {
      this.title.setTitle(translatedTitle);
    });


    this.form = fb.group({
      title: [''],
      status: ['']
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

  fetchOrders(keepUrl: boolean = false){
    this.pagination$ = this.ordersWithDataService.getSales({
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
        skipLocationChange: true,
        queryParams: {
          page: 1,
          title: this.form.get('title')?.value,
          status: this.form.get('status')?.value
        },
        queryParamsHandling: 'merge',
      });

  }

  protected readonly Object = Object;
  protected readonly OrderStatus = OrderStatus;
}
