import { Component } from '@angular/core';
import {ContentCardComponent} from "../../shared/components/content-card/content-card.component";
import {TabComponent} from "../../shared/components/tab/tab.component";
import {WriterCategory} from "../../shared/model/user/writerCategory";
import {MatGridListModule} from "@angular/material/grid-list";
import {CurrencyPipe} from "@angular/common";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {FormsModule} from "@angular/forms";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";

@Component({
  selector: 'app-analytics',
  imports: [TabComponent, MatGridListModule, CurrencyPipe, MatCheckboxModule, FormsModule, MatFormFieldModule, MatSelectModule],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent {
  showByMonth = false;
  selectedMonth: string = 'February';
  selectedYear: number = 2025;

  years = [2024, 2025];
  months = [
    'January', 'February', 'March', 'April', 'May', 'June',
    'July', 'August', 'September', 'October', 'November', 'December'
  ];

  user = {
    writerCategory: WriterCategory.DEFAULT,
    bronzeMin: 10,
    silverMin: 10,
    goldMin: 10,
    totalOrders: 6,
    totalRevenue: 300,
    ordersThisMonth: 3,
    revenueThisMonth: 150,
  }

  books = [{
    book: {
      id: 1,
      title: 'Test Book',
      author: 'Luca Bloise',
      price: 399,
      coverUrl: 'assets/book-cover.jpg'
    },
    totalOrders: 3,
    totalRevenue: 100,
  }];

}
