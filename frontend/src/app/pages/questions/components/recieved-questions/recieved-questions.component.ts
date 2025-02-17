import {Component, inject, input, OnInit} from '@angular/core';
import {QuestionCardComponent} from "../question-card/question-card.component";
import {MatCheckbox, MatCheckboxChange} from "@angular/material/checkbox";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {QuestionWithDataService} from "../../store/question-with-data.service";
import {ActivatedRoute, Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {QuestionWithData} from "../../../../shared/model/question/questionWithData";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {NgxPaginationModule} from "ngx-pagination";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-recieved-questions',
	imports: [
		QuestionCardComponent,
		MatCheckbox,
		AsyncPipe,
		PaginatorComponent,
		NgxPaginationModule,
		JsonPipe,
		TranslateModule
	],
  templateUrl: './recieved-questions.component.html',
  styleUrl: './recieved-questions.component.scss'
})
export class RecievedQuestionsComponent implements OnInit{
  questionWithDataService = inject(QuestionWithDataService);
  route = inject(ActivatedRoute);
  router = inject(Router);

  currentPage!: number;
  pageSize = 10;
  pagination$!: Observable<PaginatedContent<QuestionWithData>>;
  questions$!: Observable<QuestionWithData[]>;
  includeAnswered: boolean = true;


	setQuestions(event: MatCheckboxChange){
	    this.currentPage = 1;
	    this.includeAnswered = event.checked;
		this.pagination$ =  this.questionWithDataService.getReceivedQuestions(this.currentPage, this.pageSize, this.includeAnswered)
		this.questions$ = this.pagination$.pipe(
		  map((page) => page.data)
		)
	}

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.currentPage = Number(params['page']) || 1;
    });

    this.pagination$ =  this.questionWithDataService.getReceivedQuestions(this.currentPage, this.pageSize, this.includeAnswered)
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


		this.questions$ = this.questionWithDataService.getAskedQuestions(page, this.pageSize).pipe(
			map((page) => page.data)
		)
	}
}
