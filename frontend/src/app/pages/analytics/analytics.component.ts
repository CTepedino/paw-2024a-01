import { Component } from '@angular/core';
import {ContentCardComponent} from "../../shared/components/content-card/content-card.component";
import {TabComponent} from "../../shared/components/tab/tab.component";
import {WriterCategory} from "../../shared/model/user/writerCategory";
import {MatGridListModule} from "@angular/material/grid-list";
import {CurrencyPipe} from "@angular/common";

@Component({
  selector: 'app-analytics',
  imports: [TabComponent, MatGridListModule, CurrencyPipe],
  templateUrl: './analytics.component.html',
  styleUrl: './analytics.component.scss'
})
export class AnalyticsComponent {

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

}
