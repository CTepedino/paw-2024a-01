import { Injectable } from '@angular/core';
import {OrderService} from "./order.service";
import {UserService} from "./user.service";
import {BookService} from "./book.service";
import {Book} from "../model/book/book";
import {User} from "../model/user/user";
import {catchError, concatMap, forkJoin, map, Observable, of} from "rxjs";
import {PaginatedContent} from "../model/paginatedContent";
import {AuthService} from "./auth.service";
import {OrderSearchQuery} from "../model/order/orderSearchQuery";
import {OrderWithData} from "../model/order/orderWithData";
import {Order} from "../model/order/order";

@Injectable({
  providedIn: 'root'
})
export class OrderWithDataService {

  constructor(private orderService: OrderService, private userService: UserService, private bookService: BookService, private authService: AuthService) {}

  books: Map<string, Book> = new Map<string, Book>;


  getSales(query: OrderSearchQuery): Observable<PaginatedContent<OrderWithData>>{
    return this.authService.getLoggedUser().pipe(
      concatMap(user => this.getOrders({
        ...query,
        seller_id: user?.id!,
      }).pipe(
          concatMap(ordersPage => this.fillBuyerInfo(ordersPage))
      ))
  );
  }

  getPurchases(query: OrderSearchQuery): Observable<PaginatedContent<OrderWithData>>{
    return this.authService.getLoggedUser().pipe(
        concatMap(user => this.getOrders({
          ...query,
          buyer_id: user?.id!
        }).pipe(
            concatMap(ordersPage => this.fillWriterInfo(ordersPage))
        ))
    );
  }

  private getOrders(query: OrderSearchQuery): Observable<PaginatedContent<OrderWithData>> {
    return this.orderService.listOrders(query).pipe(
        concatMap((orders) => {
          const orderRequests = orders.data.map(o => this.fillBookInfo(o));

          return forkJoin(orderRequests).pipe(
              map((questionWithData) => ({
                data: questionWithData,
                pagination: orders.pagination
              }))
          )
        })
    )
  }

  private fillWriterInfo(orders: PaginatedContent<OrderWithData>): Observable<PaginatedContent<OrderWithData>> {
    const writerRequests = orders.data.map(o => this.userService.getUser(o.seller!).pipe(
        map(writer => ({...o, writerInfo: writer}))
    ));

    return forkJoin(writerRequests).pipe(
        map((ordersWithData) => ({
          data: ordersWithData,
          pagination: orders.pagination
        }))
    )
  }

  private fillBuyerInfo(orders: PaginatedContent<OrderWithData>): Observable<PaginatedContent<OrderWithData>> {
    const questionerRequests = orders.data.map(o => this.userService.getUser(o.buyer!).pipe(
        map(user => ({...o, buyerInfo: user}))
    ));

    return forkJoin(questionerRequests).pipe(
        map((ordersWithData) => ({
          data: ordersWithData,
          pagination: orders.pagination
        }))
    )
  }


  private fillBookInfo(order: Order): Observable<OrderWithData> {
    return this.fetchBook(order.book!).pipe(
        map(book => ({
          ...order,
          bookInfo: book
        }))
    )
  }

  private fetchBook(bookUrl: string): Observable<Book>{
    if (this.books.has(bookUrl)){
      return of(this.books.get(bookUrl)!);
    }
    return this.bookService.getBook(bookUrl).pipe(
        map((book) => {
          this.books.set(bookUrl, book);
          return book;
        })
    );
  }


}
