import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {User} from "../model/user/user";
import {Observable} from "rxjs";
import {WriterMonthlyAnalytics} from "../model/user/writerMonthlyAnalytics";
import {Wishlist} from "../model/user/wishlist";
import {Recommendation} from "../model/user/recommendation";

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiURL = `${environment.apiURL}/users`;

  constructor(private http: HttpClient) { }

  postUser(user: User){
    this.http.post(
        this.apiURL,
        user,
        {headers: {"Content-Type": "application/vnd.users.v1+json"}}
    );
  }

  getUser(userUrl: string): Observable<User>{
    return this.http.get<User>(userUrl);
  }

  putUser(userUrl: string, user: User){
    this.http.put(
        this.apiURL,
        user,
        {headers: {"Content-Type": "application/vnd.users.v1+json"}}
    );
  }

  putPassword(userUrl: string, password: string){
    this.http.put(
        `${this.apiURL}/password`,
        {password: password},
        {headers: {"Content-Type": "application/vnd.users.password.v1+json"}}
    );
  }

  putProfilePicture(userUrl: string, pfp: File){
    const formData = new FormData();
    formData.append('image', pfp);
    this.http.put(`${userUrl}/profile-picture`, formData);
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

  getWishlist(userUrl: string, page: number = 1, size: number = 20): Observable<Wishlist[]>{
    const params = new HttpParams()
        .append("page", page)
        .append("size", size);
    return this.http.get<Wishlist[]>(`${userUrl}/wishlist`, {params: params});
  }

  getWishlistItem(userUrl: string, bookId: number): Observable<Wishlist>{
    return this.http.get<Wishlist>(`${userUrl}/wishlist/${bookId}`);
  }

  postWishlistItem(userUrl: string, bookId: number){
    this.http.post(
        `${userUrl}/wishlist`,
        {bookId: bookId},
        {headers: {"Content-Type": "application/vnd.users.wishlists.v1+json"}}
    );
  }

  deleteWishlistItem(userUrl: string, bookId: number){
    this.http.delete(`${userUrl}/wishlist/${bookId}`);
  }

  getRecommendations(userUrl: string, page: number = 1, size: number = 20): Observable<Recommendation[]> {
    const params = new HttpParams()
        .append("page", page)
        .append("size", size);
    return this.http.get<Wishlist[]>(`${userUrl}/recommendations`, {params: params});
  }

  getRecommendation(userUrl: string, bookId: number): Observable<Recommendation>{
    return this.http.get<Recommendation>(`${userUrl}/recommendations/${bookId}`);
  }

  postRecommendation(userUrl: string, bookId: number){
    this.http.post(
        `${userUrl}/recommendations`,
        {bookId: bookId},
        {headers: {"Content-Type": "application/vnd.users.recommendations.v1+json"}}
    );
  }

  deleteRecommendation(userUrl: string, bookId: number){
    this.http.delete(`${userUrl}/recommendations/${bookId}`);
  }
}

