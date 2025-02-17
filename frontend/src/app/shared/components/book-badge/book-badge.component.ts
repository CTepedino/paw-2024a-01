import {Component, input} from '@angular/core';
import {SalesCategory} from "../../model/book/salesCategory";
import {TranslateModule, TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-book-badge',
    imports: [
        TranslatePipe,
        TranslateModule
    ],
  templateUrl: './book-badge.component.html',
  standalone: true,
  styleUrl: './book-badge.component.scss'
})
export class BookBadgeComponent {
  category = input<SalesCategory>();
  protected readonly SalesCategory = SalesCategory;
}
