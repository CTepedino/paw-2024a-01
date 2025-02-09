import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AsyncPipe, JsonPipe, NgOptimizedImage} from "@angular/common";
import {Observable} from "rxjs";
import {AuthService} from "./shared/services/auth.service";
import {Book} from "./shared/model/book/book";
import {Index} from "./shared/model";
import {BookService} from "./shared/services/book.service";
import {BookSearchQuery} from "./shared/model/book/bookSearchQuery";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JsonPipe, AsyncPipe, NgOptimizedImage],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  data$: Observable<Book[]>
  //index$: Observable<Index>;

  constructor(private authService: AuthService, private bookService: BookService) {
    //this.index$ = this.authService.login("apitest@mail.com", "123456");
    this.data$ = this.bookService.listBooks({title: "du"} as BookSearchQuery);
   // this.authService.sendResetPasswordCodeEmail("apitest@mail.com").subscribe();
  }

}
