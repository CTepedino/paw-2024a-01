import {Component, inject, input, OnInit} from '@angular/core';
import {QuestionWithData} from "../../../../shared/model/question/questionWithData";
import {QuestionCardComponent} from "../question-card/question-card.component";
import {MatFormField} from "@angular/material/form-field";
import {MatCheckbox} from "@angular/material/checkbox";
import {map, Observable} from "rxjs";

import {ActivatedRoute, Router} from "@angular/router";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {NgxPaginationModule} from "ngx-pagination";
import {TranslateModule} from "@ngx-translate/core";
import {QuestionWithDataService} from "../../../../shared/services/question-with-data.service";

@Component({
  selector: 'app-asked-questions',
    imports: [
        QuestionCardComponent,
        MatFormField,
        MatCheckbox,
        AsyncPipe,
        PaginatorComponent,
        NgxPaginationModule,
        JsonPipe,
        TranslateModule
    ],
  templateUrl: './asked-questions.component.html',
  styleUrl: './asked-questions.component.scss'
})
export class AskedQuestionsComponent implements OnInit {
    questionWithDataService = inject(QuestionWithDataService);
    route = inject(ActivatedRoute);
    router = inject(Router);

    currentPage!: number;
    pageSize = 10;
    pagination$!: Observable<PaginatedContent<QuestionWithData>>;
    questions$!: Observable<QuestionWithData[]>;

    ngOnInit(): void {
        this.route.queryParams.subscribe(params => {
            this.currentPage = Number(params['page']) || 1;
        });

        this.pagination$ =  this.questionWithDataService.getAskedQuestions(this.currentPage, this.pageSize)
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
