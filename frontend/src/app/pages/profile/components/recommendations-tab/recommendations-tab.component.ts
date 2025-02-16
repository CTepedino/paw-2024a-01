import {Component, inject, input, OnInit} from '@angular/core';
import {AsyncPipe} from "@angular/common";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {NgxPaginationModule} from "ngx-pagination";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {SmallBookCardComponent} from "../../../../shared/components/small-book-card/small-book-card.component";
import {ActivatedRoute, Router} from "@angular/router";
import {UserProfileService} from "../../store/user-profile.service";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {BookWithData} from "../../../../shared/model/book/bookWithData";

@Component({
  selector: 'app-recommendations-tab',
  imports: [
    AsyncPipe,
    MatGridList,
    MatGridTile,
    NgxPaginationModule,
    PaginatorComponent,
    SmallBookCardComponent
  ],
  templateUrl: './recommendations-tab.component.html',
  styleUrl: './recommendations-tab.component.scss'
})
export class RecommendationsTabComponent implements OnInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private profileService = inject(UserProfileService);

  userId = input.required<number>();

  pagination$!: Observable<PaginatedContent<BookWithData>>;
  books$!: Observable<BookWithData[]>
  currentPage!: number;
  pageSize = 20;

  ngOnInit() {
    this.route.queryParams.subscribe(params => {
      this.currentPage =Number(params['page']) || 1;
    });
    this.pagination$ =  this.profileService.getRecommendations(this.userId(), this.currentPage, this.pageSize)
    this.books$ = this.pagination$.pipe(
        map((page) => page.data)
    )
  }

  onPageChange(page: number){
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: page },
      queryParamsHandling: 'merge',
    });

    this.currentPage = page;

    this.books$ = this.profileService.getRecommendations(this.userId(), this.currentPage, this.pageSize).pipe(
        map((page) => page.data)
    )
  }
}
