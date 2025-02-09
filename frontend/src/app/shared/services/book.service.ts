import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {BookSearchQuery} from "../model/book/bookSearchQuery";
import {Observable} from "rxjs";
import {Book} from "../model/book/book";
import {BookMonthlyAnalytics} from "../model/book/bookMonthlyAnalytics";
import {Deal} from "../model/book/deal";
import {ReviewSearchQuery} from "../model/book/reviewSearchQuery";
import {Review} from "../model/book/review";
import {MediaTypes} from "../const/mediaTypes";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiURL = `${environment.apiURL}/books`;

  constructor(private http: HttpClient) {}

  listBooks(query: BookSearchQuery): Observable<Book[]> {
     let params = new HttpParams();

     Object.entries(query).forEach(([name, value]) => {
       if (value !== null && value !== undefined){
         params = params.append(name, value.toString());
       }
    })

     return this.http.get<Book[]>(this.apiURL, {params: params});
  }

  postBook(book: Book) {
      this.http.post(
          this.apiURL,
          book,
          {headers: {"Content-Type": MediaTypes.BOOK}}
      );
  }

  getBook(bookUrl: string): Observable<Book> {
      return this.http.get<Book>(bookUrl);
  }

  putBook(bookUrl: string, book: Book){
      this.http.put(
          bookUrl,
          book,
          {headers: {"Content-Type": MediaTypes.BOOK}}
      );
  }

  putBookCover(bookUrl: string, cover: File){
      const formData = new FormData();
      formData.append('cover', cover);
      this.http.put(`${bookUrl}/cover`, formData);
  }

  putBookPreview(bookUrl: string, preview: File){
      const formData = new FormData();
      formData.append('preview', preview);
      this.http.put(`${bookUrl}/preview`, formData);
  }

  putBookFile(bookUrl: string, file: File){
      const formData = new FormData();
      formData.append('book_file', file);
      this.http.put(`${bookUrl}/book-file`, formData);
  }

  getDeal(dealUrl: string): Observable<Deal> {
      return this.http.get<Deal>(dealUrl);
  }

  putDeal(bookUrl: string, deal: Deal){
      this.http.put(
          `${bookUrl}/deal`,
          deal,
          {headers: {"Content-Type": MediaTypes.DEAL}}
      );
  }

  deleteDeal(dealUrl: string){
      this.http.delete(dealUrl);
  }

  getBookMonthlyAnalytics(analyticsUrl: string): Observable<BookMonthlyAnalytics> {
      return this.http.get<BookMonthlyAnalytics>(analyticsUrl);
  }

  getBookMonthlyAnalyticsFromBook(bookUrl: string, period: string): Observable<BookMonthlyAnalytics> {
      return this.http.get<BookMonthlyAnalytics>(`${bookUrl}/monthly-analytics/${period}`);
  }

  listReviews(bookUrl: string, query: ReviewSearchQuery): Observable<Review[]>{
      const params = new HttpParams();

      Object.entries(query).forEach(([name, value]) => {
          if (value !== null && value !== undefined){
              params.append(name, value);
          }
      });

      return this.http.get<Review[]>(bookUrl, {params: params});
  }

  getReview(bookUrl: string, userId: number): Observable<Review> {
      return this.http.get<Review>(`${bookUrl}/reviews/${userId}`);
  }

  putReview(bookUrl: string, userId: number, review: Review){
      return this.http.put(
          `${bookUrl}/reviews/${userId}`,
          review,
          {headers: {"Content-Type": MediaTypes.REVIEW}}
      );
  }
}
