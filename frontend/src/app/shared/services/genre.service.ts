import { Injectable } from '@angular/core';
import {HttpClient, HttpParams, HttpResponse} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {BookGenre} from "../model/book/bookGenre";
import {map, Observable} from "rxjs";
import {PaginatedContent, setPagination} from "../model/paginatedContent";

@Injectable({
  providedIn: 'root'
})
export class GenreService {

  private apiURL = `${environment.apiURL}/genres`;

  constructor(private http: HttpClient) { }

  getGenresByBookCount(page: number = 1, size: number = 12): Observable<PaginatedContent<BookGenre>> {
    const params = new HttpParams()
        .append("page", page)
        .append("size", size)
        .append("order_by", "BOOK_COUNT");
    return this.http.get<BookGenre[]>(this.apiURL, {params: params, observe: 'response'}).pipe(
        map((response: HttpResponse<BookGenre[]>) => setPagination(response, size))
    );
  }
}
