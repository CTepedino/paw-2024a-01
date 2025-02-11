import {Component, input, output} from '@angular/core';
import {NgxPaginationModule} from "ngx-pagination";


@Component({
  selector: 'app-paginator',
  imports: [
    NgxPaginationModule

  ],
  templateUrl: './paginator.component.html',
  styleUrl: './paginator.component.scss'
})
export class PaginatorComponent {
  id = input.required<string>();
  pageChange = output<number>();

  handlePageEvent(e: number) {
    this.pageChange.emit(e);
  }
}
