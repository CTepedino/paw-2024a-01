import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AsyncPipe, JsonPipe, NgOptimizedImage} from "@angular/common";
import {Observable} from "rxjs";
import {AuthService} from "./shared/services/auth.service";
import {Book} from "./shared/model/book/book";
import {BookService} from "./shared/services/book.service";
import {BookSearchQuery} from "./shared/model/book/bookSearchQuery";
import {NavbarComponent} from "./shared/components/navbar/navbar.component";

@Component({
  selector: 'app-root',
	imports: [RouterOutlet, JsonPipe, AsyncPipe, NgOptimizedImage, NavbarComponent],
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
