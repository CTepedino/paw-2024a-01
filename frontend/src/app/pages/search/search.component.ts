import {Component, inject} from "@angular/core";
import {FormsModule} from "@angular/forms";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {HomeService} from "../home/store/home.service";
import {ActivatedRoute, Router} from "@angular/router";
import {map, Observable} from "rxjs";
import {PaginatedContent} from "../../shared/model/paginatedContent";
import {BookWithInfo} from "../../shared/model/book/bookWithInfo";
import {AsyncPipe} from "@angular/common";
import {NgxPaginationModule} from "ngx-pagination";
import {PaginatorComponent} from "../../shared/components/paginator/paginator.component";
import {BookCardComponent} from "../../shared/components/book-card/book-card.component";
import {Book} from "../../shared/model/book/book";
import {SmallBookCardComponent} from "../../shared/components/small-book-card/small-book-card.component";
import {MatCardModule} from "@angular/material/card";
import {OrderStatus} from "../../shared/model/order/orderStatus";
import {BookCardSerachComponent} from "./components/book-card-search/book-card-serach.component";

@Component({
    selector: 'app-search',
    imports: [FormsModule, MatGridList, MatGridTile, MatCardModule, AsyncPipe, NgxPaginationModule, BookCardSerachComponent],
    templateUrl: './search.component.html',
    styleUrl: './search.component.scss',
})
export class SearchComponent {

    book = {
        cover: "assets/book-cover.jpg",
        title: "Test book",
        price: 300,
        author: "Juan Lopez",
        genre: "Fiction",
        age: 15,
        pages: 200,
        year: 2024,
        percentage: 20,
        deal: 200
    }


    books = Array(10).fill(this.book);
    //orders = Array()
}
