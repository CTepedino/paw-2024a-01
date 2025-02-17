import {Component, input} from '@angular/core';
import {CurrencyPipe, NgOptimizedImage} from "@angular/common";
import {BookWithAnalytics} from "../../store/analytics.service";
import {RouterLink} from "@angular/router";
import {SalesCategory} from "../../../../shared/model/book/salesCategory";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-analytics-card',
	imports: [
		CurrencyPipe,
		NgOptimizedImage,
		RouterLink,
		TranslateModule
	],
  templateUrl: './analytics-card.component.html',
  styleUrl: './analytics-card.component.scss'
})
export class AnalyticsCardComponent {
	book = input.required<BookWithAnalytics>();
	protected readonly SalesCategory = SalesCategory;
}
