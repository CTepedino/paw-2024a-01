import {Component, inject, input, OnInit} from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {BookWithData} from "../../../../shared/model/book/bookWithData";
import {map, Observable} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";
import {PaginatedContent} from "../../../../shared/model/paginatedContent";
import {UserProfileService} from "../../store/user-profile.service";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {SmallBookCardComponent} from "../../../../shared/components/small-book-card/small-book-card.component";
import {PaginatorComponent} from "../../../../shared/components/paginator/paginator.component";
import {NgxPaginationModule} from "ngx-pagination";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-wishlist-tab',
  imports: [
    MatGridTile,
    AsyncPipe,
    JsonPipe,
    MatGridList,
    SmallBookCardComponent,
    PaginatorComponent,
    NgxPaginationModule,
    TranslateModule
  ],
  templateUrl: './wishlist-tab.component.html',
  styleUrl: './wishlist-tab.component.scss'
})
export class WishlistTabComponent implements OnInit{
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
    this.pagination$ =  this.profileService.getWishlist(this.userId(), this.currentPage, this.pageSize)
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

    this.books$ = this.profileService.getWishlist(this.userId(), this.currentPage, this.pageSize).pipe(
        map((page) => page.data)
    )
  }

}
