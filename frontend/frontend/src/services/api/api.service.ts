import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from "@angular/common/http";
import { Observable } from "rxjs";
import {environment} from "../../enviroment/enviroment";
import {Book} from "../../model/book/book";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiURL;

  constructor(private http: HttpClient) { }

  get(path: string, params?: Record<string, string>): Observable<Book> {
    let httpParams = new HttpParams();
    let httpHeaders = new HttpHeaders({Authorization: 'Basic ' + btoa("apitest@mail.com:123456")});

    if (params){
      Object.keys(params).forEach(key => {
        httpParams = httpParams.set(key, params[key]);
      });
    }


    return this.http.get<Book>(
        `${this.baseUrl}${path}`,
        {params: httpParams, headers: httpHeaders}
    );
  }

}
