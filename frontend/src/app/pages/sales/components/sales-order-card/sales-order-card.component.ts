import {Component, inject} from '@angular/core';
import {OrderStatus} from "../../../../shared/model/order/orderStatus";
import {MatGridListModule} from "@angular/material/grid-list";
import {CurrencyPipe, DatePipe} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {AcceptPopupComponent} from "../accept-popup/accept-popup.component";
import {DeclinePopupComponent} from "../decline-popup/decline-popup.component";

@Component({
  selector: 'app-sales-order-card',
  imports: [MatGridListModule, CurrencyPipe, DatePipe, MatButtonModule],
  templateUrl: './sales-order-card.component.html',
  styleUrl: './sales-order-card.component.scss'
})
export class SalesOrderCardComponent {
  readonly dialog = inject(MatDialog);

  openAcceptDialog(): void {
    this.dialog.open(AcceptPopupComponent, {
      width: '99%',
      height: '175px',
    });
  }

  openDeclineDialog(): void {
    this.dialog.open(DeclinePopupComponent, {
      width: '99%',
      height: '260px',
    });
  }

  user = {
    firstName: 'Juan',
    lastName: 'Perez'
  }

  book = {
    cover: "assets/book-cover.jpg",
    name: "Test book",
    price: 300
  }

  order = {
    book: this.book,
    buyer: this.user,
    date: '2023-05-05',
    status: OrderStatus.WAITING_APPROVAL
  }

  orders = Array(10).fill(this.order);

  protected readonly OrderStatus = OrderStatus;
}
