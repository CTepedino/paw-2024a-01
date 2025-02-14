import {Component, inject} from "@angular/core";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {AsyncPipe} from "@angular/common";
import {NgxPaginationModule} from "ngx-pagination";
import {MatCardModule} from "@angular/material/card";
import {BookCardSerachComponent} from "./components/book-card-search/book-card-serach.component";
import { MatSelectModule } from '@angular/material/select';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import {BookGenre} from "../../shared/model/book/bookGenre";
import {BookSearchOrderBy} from "../../shared/model/book/bookSearchOrderBy";
import {ActionButtonComponent} from "../../shared/components/action-button/action-button.component";


@Component({
    selector: 'app-search',
    imports: [FormsModule, MatGridList, MatGridTile, MatCardModule, AsyncPipe, NgxPaginationModule, BookCardSerachComponent, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule, ReactiveFormsModule, ActionButtonComponent],
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
    generos = ['Ficción', 'No ficción', 'Misterio', 'Ciencia Ficción', 'Fantasía', 'Romance'];
    generoSeleccionado: string = '';
    protected readonly BookGenre = BookGenre;
    protected readonly Object = Object;
    protected readonly BookSearchOrderBy = BookSearchOrderBy;
}
