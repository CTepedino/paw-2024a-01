import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {ApiService} from "../services/api.service";
import {AsyncPipe, JsonPipe} from "@angular/common";
import {Observable} from "rxjs";

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, JsonPipe, AsyncPipe],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss',
})
export class AppComponent {
  data$: Observable<any>;

  constructor(private apiService: ApiService) {
    this.data$ = this.apiService.getData();
  }


}
