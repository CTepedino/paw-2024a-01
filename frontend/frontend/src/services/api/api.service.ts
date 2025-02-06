import { Injectable } from '@angular/core';
import {HttpClient, HttpParams} from "@angular/common/http";
import { Observable } from "rxjs";
import {environment} from "../../enviroment/enviroment";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private baseUrl = environment.apiURL;

  constructor(private http: HttpClient) { }

  get(path: string, params?: Record<string, string>): Observable<any> {
    let httpParams = new HttpParams();

    return this.http.get<any>(
        `${this.baseUrl}/${path}`,
        {params: httpParams}
    );
  }

}
