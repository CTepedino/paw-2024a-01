import {Component, inject, OnInit} from '@angular/core';
import {CommonModule, NgOptimizedImage} from '@angular/common';
import {MatIconModule} from '@angular/material/icon';
import {ActivatedRoute, Router, RouterModule} from '@angular/router';
import {StarRatingComponent} from '../../shared/components/star-rating/star-rating.component';
import {ActionButtonComponent} from '../../shared/components/action-button/action-button.component';
import {SalesCategory} from '../../shared/model/book/salesCategory';
import {ContentCardComponent} from "../../shared/components/content-card/content-card.component";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {WriterCategory} from "../../shared/model/user/writerCategory";
import {MatCheckbox} from "@angular/material/checkbox";
import {GenreIcon} from "../../shared/const/genreIcon";
import {Title} from "@angular/platform-browser";
import {SmallBookCardComponent} from "../../shared/components/small-book-card/small-book-card.component";
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {BookDetailsService} from "./store/book-details.service";
import {concatMap, Observable, tap} from "rxjs";
import {BookWithData} from "../../shared/model/book/bookWithData";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {ReviewWithInfo} from "../../shared/model/review/reviewWithInfo";
import {MatDialog} from "@angular/material/dialog";
import {ReviewFormCardComponent} from "./components/review-form-card/review-form-card.component";
import {ReviewTabComponent} from "./components/review-tab/review-tab.component";
import {QuestionsTabComponent} from "./components/questions-tab/questions-tab.component";
import {MyQuestionsTabComponent} from "./components/my-questions-tab/my-questions-tab.component";
import {WriterQuestionsTabComponent} from "./components/writer-questions-tab/writer-questions-tab.component";
import {QuestionSubmitBarComponent} from "./components/question-submit-bar/question-submit-bar.component";
import {QuestionWithData} from "../../shared/model/question/questionWithData";

@Component({
    selector: 'app-book-details',
    standalone: true,
    imports: [
        CommonModule,
        MatIconModule,
        RouterModule,
        ContentCardComponent,
        MatGridList,
        MatGridTile,
        NgOptimizedImage,
        StarRatingComponent,
        ActionButtonComponent,
        MatCheckbox,
        SmallBookCardComponent,
        MatTabGroup,
        MatTab,
        MatTabContent,
        MatTabLabel,
        ReviewTabComponent,
        QuestionsTabComponent,
        MyQuestionsTabComponent,
        WriterQuestionsTabComponent,
        QuestionSubmitBarComponent
    ],
    templateUrl: './book-details.component.html',
    styleUrl: './book-details.component.scss'
})
export class BookDetailsComponent implements OnInit {
    dialog = inject(MatDialog);
    bookDetailsService = inject(BookDetailsService);
    route = inject(ActivatedRoute);
    router = inject(Router);
    title = inject(Title);

    recommendedPageSize = 4;
    pageSize = 10;

    pageNumber = 1;

    bookId: number = 0;
    book$: Observable<BookWithData> | null = null;
    recommendations$: Observable<BookWithData[]> | null = null;
    isLoggedIn$: Observable<boolean> | null = null;
    isAuthor$: Observable<boolean> | null = null;
    existsOrder$: Observable<boolean> | null = null;
    isOwner$: Observable<boolean> | null = null;

    isWishlisted: boolean = false;
    isRecommended: boolean = false;

    loggedUserReview: ReviewWithInfo | null = null;

    reviewPage$: Observable<PaginatedContent<ReviewWithInfo>> | null = null;

    questionPage$: Observable<PaginatedContent<QuestionWithData>> | null = null;

    myQuestionPage$: Observable<PaginatedContent<QuestionWithData>> | null = null;

    setup = true;
    index = { selectedIndex: 0 }

    constructor() {
        this.title.setTitle('Book details')
    }

    ngOnInit() {
        this.route.params.subscribe((params) => {
            this.bookId = params['id'];
            this.book$ = this.bookDetailsService.getBook(this.bookId);
            this.recommendations$ = this.bookDetailsService.getRecommendedBooks(this.bookId, this.recommendedPageSize);
            this.reviewPage$ = this.bookDetailsService.getReviews(this.bookId,{page: this.pageNumber, size: this.pageSize});
            this.isLoggedIn$ = this.bookDetailsService.isLoggedIn().pipe(
                tap(logged => {
                    if (logged){
                        this.isAuthor$ = this.bookDetailsService.isAuthor(this.bookId).pipe(
                            tap(isAuthor => {
                                if (!isAuthor){
                                    this.questionPage$ = this.bookDetailsService.getAllOtherQuestions(this.bookId, this.pageNumber, this.pageSize);
                                    this.myQuestionPage$ = this.bookDetailsService.getAllMyQuestions(this.bookId, this.pageNumber, this.pageSize);
                                    this.bookDetailsService.getLoggedReview(this.bookId).subscribe(r => this.loggedUserReview = r);
                                } else {
                                    this.questionPage$ = this.bookDetailsService.getAllQuestions(this.bookId, this.pageNumber, this.pageSize);
                                }
                            })
                        );
                        this.existsOrder$ = this.bookDetailsService.existsOrder(this.bookId);
                        this.isOwner$ = this.bookDetailsService.isOwner(this.bookId);
                        this.bookDetailsService.isWishlisted(this.bookId).subscribe(w => this.isWishlisted = w);
                        this.bookDetailsService.isRecommended(this.bookId).subscribe(r => this.isRecommended = r);

                    } else {
                        this.questionPage$ = this.bookDetailsService.getAllOtherQuestions(this.bookId, this.pageNumber, this.pageSize);
                    }
                })
            );

        })

        const url = this.router.url;
        if (url.includes('questions')) {
            setTimeout(() => this.index.selectedIndex = 1, 100);
        }
        if (url.includes('my-questions')) {
            setTimeout(() => this.index.selectedIndex = 2, 100);
        }
        setTimeout(() => this.setup = false, 150);
    }

    getPercentage(price: number, dealPrice: number){
        return ((price-dealPrice)/price)*100;
    }

    openReviewDialog(){
        const reviewDialog = this.dialog.open(ReviewFormCardComponent, {
            width: '99%',
            height: '500px',
            data: {review: this.loggedUserReview}
        })

        reviewDialog.afterClosed().subscribe(result => {
            if (result.update){
                this.bookDetailsService.setReview(this.bookId, result.review).pipe(
                    concatMap(() => this.bookDetailsService.getLoggedReview(this.bookId))
                ).subscribe(review => this.loggedUserReview = review);
            }
        })
    }

    refetchMyQuestions(){
        this.myQuestionPage$ = this.bookDetailsService.getAllMyQuestions(this.bookId, this.pageNumber, this.pageSize);
    }


    onTabChange(event: any){
        if (!this.setup){
            this.pageNumber = 1;
        }
        if (event.index === 0) {
            this.router.navigate([`book/${this.bookId}/reviews`], {queryParams: {page: this.pageNumber}, queryParamsHandling: 'replace'});
        } else if (event.index === 1) {
            this.router.navigate([`book/${this.bookId}/questions`], {queryParams: {page: this.pageNumber}, queryParamsHandling: 'replace'});
        } else if (event.index === 2) {
            this.router.navigate([`book/${this.bookId}/my-questions`], {queryParams: {page: this.pageNumber}, queryParamsHandling: 'replace'});
        }
    }

    protected readonly SalesCategory = SalesCategory;
    protected readonly WriterCategory = WriterCategory;
    protected readonly GenreIcon = GenreIcon;
}