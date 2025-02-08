import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ApiService} from "./shared/services/api.service";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {Observable} from "rxjs";
import {ApiAuthService} from "./shared/services/api-auth.service";
import {Book} from "./shared/model/book/book";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JsonPipe, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  data$: Observable<Book>;


  constructor(private apiService: ApiService, private authService: ApiAuthService) {
    this.data$ = this.apiService.get("/books/105");

    this.authService.login("apitest@mail.com", "123456").subscribe();
  }



}
