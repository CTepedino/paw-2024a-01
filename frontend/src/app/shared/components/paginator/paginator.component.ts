import {Component, EventEmitter, input, output, ViewChild} from '@angular/core';
import {MatPaginator, PageEvent} from "@angular/material/paginator";
import {BubblePaginationDirective} from "../../directives/bubble-pagination.directive";

@Component({
  selector: 'app-paginator',
  imports: [
    MatPaginator,
    BubblePaginationDirective
  ],
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss'
})
export class PaginatorComponent {
  page = input<number>(1);
  size = input<number>(10);
  total = input.required<number>();

  pageChange = output<number>();

  handlePageEvent(e: PageEvent) {
    this.pageChange.emit(e.pageIndex + 1);
  }
}
