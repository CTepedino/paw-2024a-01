import {Component, inject, input, OnInit} from '@angular/core';
import {BookDetailsService} from "../../store/book-details.service";
import {ActivatedRoute, Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {QuestionWithData} from "../../../../shared/model/question/questionWithData";
import {AsyncPipe} from "@angular/common";
import {NgxPaginationModule} from "ngx-pagination";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {SmallQuestionCardComponent} from "../small-question-card/small-question-card.component";
import {TranslatePipe} from "@ngx-translate/core";

@Component({
  selector: 'app-writer-questions-tab',
  imports: [
    AsyncPipe,
    NgxPaginationModule,
    PaginatorComponent,
    SmallQuestionCardComponent,
    TranslatePipe
  ],
  templateUrl: './writer-questions-tab.component.html',
  styleUrl: './writer-questions-tab.component.scss'
})
export class WriterQuestionsTabComponent implements OnInit{
  bookDetailsService = inject(BookDetailsService)
  route = inject(ActivatedRoute);
  router = inject(Router);

  bookId = input.required<number>();
  questionPage = input.required<Observable<PaginatedContent<QuestionWithData>>>();


  currentPage!: number;
  pageSize = 10;
  pagination$!: Observable<PaginatedContent<QuestionWithData>>;
  questions$!: Observable<QuestionWithData[]>;

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.currentPage = Number(params['page']) || 1;
    });

    this.pagination$ =  this.questionPage();
    this.questions$ = this.pagination$.pipe(
        map((page) => page.data)
    )
  }

  onPageChange(page: number){

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: page },
      queryParamsHandling: 'merge',
    });


    this.questions$ = this.bookDetailsService.getAllQuestions(this.bookId(), this.currentPage, this.pageSize).pipe(
        map((page) => page.data)
    )
  }
}
