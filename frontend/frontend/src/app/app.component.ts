import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {AsyncPipe, JsonPipe} from "@angular/common";
import {Observable} from "rxjs";
import {ApiAuthService} from "./shared/services/api-auth.service";
import {Book} from "./shared/model/book/book";
import {Index} from "./shared/model";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JsonPipe, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  index$: Observable<Index>;

  constructor(private authService: ApiAuthService) {
    this.index$ = this.authService.login("apitest@mail.com", "123456");

    this.authService.sendResetPasswordCodeEmail("apitest@mail.com").subscribe();
  }



}
