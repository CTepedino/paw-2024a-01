import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";
import {BookGenre} from "../model/book/bookGenre";
import {SearchQuery} from "../model/book/searchQuery";
import {Observable} from "rxjs";
import {Book} from "../model/book/book";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiURL = `${environment.apiURL}/books`;

  constructor(private http: HttpClient) {}

  listBooks(query: SearchQuery): Observable<Book[]> {
     let params = new HttpParams();

     Object.entries(query).forEach(([name, value]) => {
       if (value !== null && value !== undefined){
         params = params.append(name, value.toString());
       }
    })

     return this.http.get<Book[]>(this.apiURL, {params: params});
  }
}
