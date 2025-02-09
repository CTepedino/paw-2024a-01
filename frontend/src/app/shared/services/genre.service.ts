import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {BookGenre} from "../model/book/bookGenre";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class GenreService {

  private apiURL = `${environment.apiURL}/genres`;

  constructor(private http: HttpClient) { }

  getGenresByBookCount(page: number = 1, size: number = 12): Observable<BookGenre[]> {
    const params = new HttpParams();
    params.append("page", page);
    params.append("size", size);
    params.append("order_by", "BOOK_COUNT");
    return this.http.get<BookGenre[]>(this.apiURL, {params: params});
  }
}
