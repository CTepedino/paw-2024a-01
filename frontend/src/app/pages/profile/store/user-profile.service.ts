import {Injectable} from '@angular/core';
import {AuthService} from "../../../shared/services/auth.service";
import {UserService} from "../../../shared/services/user.service";
import {User} from "../../../shared/model/user/user";
import {catchError, concatMap, forkJoin, map, Observable, of} from "rxjs";
import {BookWithDataService} from "../../../shared/services/book-with-data.service";
import {BookWithData} from "../../../shared/model/book/bookWithData";
import {BookSearchQuery} from "../../../shared/model/book/bookSearchQuery";
import {PaginatedContent} from "../../../shared/model/paginatedContent";

@Injectable({
  providedIn: 'root'
})
export class UserProfileService {

  constructor(private authService: AuthService, private userService: UserService, private bookWithDataService: BookWithDataService) { }

  getUser(id: any): Observable<ProfileDisplayInfo>{
    return this.userService.getUserById(id).pipe(
        concatMap((user) => {
          return this.authService.getLoggedUser().pipe(
              map(loggedUser => new ProfileDisplayInfo(user, loggedUser != null && id == loggedUser.id)),
              catchError(() => of(new ProfileDisplayInfo(user, false)))
          )
        })
    );
  }


  getPublications(query: BookSearchQuery, userId: any): Observable<PaginatedContent<BookWithData>> {
    return this.bookWithDataService.listBooksWithData({
      ...query,
      writer_id: userId
    })
  }

  getBoughtBooks(query: BookSearchQuery, userId: any): Observable<PaginatedContent<BookWithData>> {
    return this.bookWithDataService.listBooksWithData({
      ...query,
      owner_id: userId
    })
  }

  getRecommendations(userId: any, page: number, size: number): Observable<PaginatedContent<BookWithData>> {
    return this.userService.getRecommendations(userId, page, size).pipe(
        concatMap(recommendations => {
          const bookRequests = recommendations.data.map(rec => this.bookWithDataService.getBookWithData(rec.bookId!));

          return forkJoin(bookRequests).pipe(
              map(books => {
                return {
                  data: books,
                  pagination: recommendations.pagination
                }
              })
          )
        })
    )
  }

  getWishlist(userId: any, page: number, size: number): Observable<PaginatedContent<BookWithData>> {
    return this.userService.getWishlist(userId, page, size).pipe(
        concatMap(wishlist => {
          const bookRequests = wishlist.data.map(w => this.bookWithDataService.getBookWithData(w.bookId!));

          return forkJoin(bookRequests).pipe(
              map(books => {
                return {
                  data: books,
                  pagination: wishlist.pagination
                }
              })
          )
        })
    )
  }

}

export class ProfileDisplayInfo {
  user: User;
  isOwner: boolean;

  constructor(user: User, isOwner: boolean) {
    this.user = user;
    this.isOwner = isOwner;
  }
}