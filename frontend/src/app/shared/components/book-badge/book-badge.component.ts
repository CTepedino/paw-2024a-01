import {Component, input} from '@angular/core';
import {SalesCategory} from "../../model/book/salesCategory";

@Component({
  selector: 'app-book-badge',
  imports: [],
  templateUrl: './book-badge.component.html',
  standalone: true,
  styleUrl: './book-badge.component.scss'
})
export class BookBadgeComponent {
  category = input<SalesCategory>();
  protected readonly SalesCategory = SalesCategory;
}
