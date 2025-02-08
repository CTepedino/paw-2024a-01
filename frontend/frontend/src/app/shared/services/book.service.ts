import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {environment} from "../../../enviroment/enviroment";

@Injectable({
  providedIn: 'root'
})
export class BookService {
  private apiURL = `${environment.apiURL}/books`;

  constructor(private http: HttpClient) {}


}
