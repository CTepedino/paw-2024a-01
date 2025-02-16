import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {User} from "../model/user/user";
import {map, Observable} from "rxjs";
import {WriterMonthlyAnalytics} from "../model/user/writerMonthlyAnalytics";
import {Wishlist} from "../model/user/wishlist";
import {Recommendation} from "../model/user/recommendation";
import {MediaTypes} from "../const/mediaTypes";
import {PaginatedContent, setPagination} from "../model/paginatedContent";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiURL = `${environment.apiURL}/users`;

  constructor(private http: HttpClient) { }

  postUser(user: User): Observable<void>{
    const headers = new HttpHeaders({
      'Accept-Language': navigator.language || 'en-US',
      "Content-Type": MediaTypes.USER
    });

    return this.http.post<void>(
        this.apiURL,
        user,
        {headers: headers}
    );
  }

  getUser(userUrl: string): Observable<User>{
    return this.http.get<User>(userUrl);
  }

  getUserById(id: number): Observable<User>{
    return this.http.get<User>(`${this.apiURL}/${id}`);
  }

  putUser(userUrl: string, user: User): Observable<void>{
    return this.http.put<void>(
        userUrl,
        user,
        {headers: {"Content-Type": MediaTypes.USER}}
    );
  }

  putPassword(userUrl: string, password: string): Observable<void>{
    return this.http.put<void>(
        `${userUrl}/password`,
        {password: password},
        {headers: {"Content-Type": MediaTypes.PASSWORD}}
    );
  }

  putProfilePicture(userUrl: string, pfp: File): Observable<void>{
    const formData = new FormData();
    formData.append('image', pfp);
    return this.http.put<void>(`${userUrl}/profile-picture`, formData);
  }

  deleteProfilePicture(userUrl: string){
    this.http.delete(`${userUrl}/profile-picture`)
  }

  getWriterMonthlyAnalytics(analyticsUrl: string): Observable<WriterMonthlyAnalytics> {
    return this.http.get<WriterMonthlyAnalytics>(analyticsUrl);
  }

  getWriterMonthlyAnalyticsFromWriter(userUrl: string, period: string): Observable<WriterMonthlyAnalytics> {
    return this.http.get<WriterMonthlyAnalytics>(`${userUrl}/monthly-analytics/${period}`);
  }

  getWishlist(userId: any, page: number = 1, size: number = 20): Observable<PaginatedContent<Wishlist>>{
    const params = new HttpParams()
        .append("page", page)
        .append("size", size);
    return this.http.get<Wishlist[]>(`${this.apiURL}/${userId}/wishlist`, {params: params, observe: "response"}).pipe(
        map(response => setPagination(response, size))
    );
  }

  getWishlistItem(userUrl: string, bookId: number): Observable<Wishlist>{
    return this.http.get<Wishlist>(`${userUrl}/wishlist/${bookId}`);
  }

  postWishlistItem(userUrl: string, bookId: number){
    this.http.post(
        `${userUrl}/wishlist`,
        {bookId: bookId},
        {headers: {"Content-Type": MediaTypes.WISHLIST}}
    );
  }

  deleteWishlistItem(userUrl: string, bookId: number){
    this.http.delete(`${userUrl}/wishlist/${bookId}`);
  }

  getRecommendations(userId: any, page: number = 1, size: number = 20): Observable<PaginatedContent<Recommendation>> {
    const params = new HttpParams()
        .append("page", page)
        .append("size", size);
    return this.http.get<Wishlist[]>(`${this.apiURL}/${userId}/recommendations`, {params: params, observe: 'response'}).pipe(
        map(response => setPagination(response, size))
    );
  }

  getRecommendation(userUrl: string, bookId: number): Observable<Recommendation>{
    return this.http.get<Recommendation>(`${userUrl}/recommendations/${bookId}`);
  }

  postRecommendation(userUrl: string, bookId: number){
    this.http.post(
        `${userUrl}/recommendations`,
        {bookId: bookId},
        {headers: {"Content-Type": MediaTypes.RECOMMENDATION}}
    );
  }

  deleteRecommendation(userUrl: string, bookId: number){
    this.http.delete(`${userUrl}/recommendations/${bookId}`);
  }
}

