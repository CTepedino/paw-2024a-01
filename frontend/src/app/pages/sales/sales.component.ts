import { Component } from '@angular/core';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";
import {OrderStatus} from "../../shared/model/order/orderStatus";

@Component({
  selector: 'app-sales',
  imports: [MatGridListModule, MatFormFieldModule, MatLabel, MatInputModule, MatSelectModule, FormsModule],
  templateUrl: './sales.component.html',
  styleUrl: './sales.component.scss',
})
export class SalesComponent {
  orderStatusOptions = [
    { label: $localize`All status`, value: 'ALL' },
    { label: $localize`Rejected payment`, value: OrderStatus.REJECTED_PAYMENT },
    { label: $localize`Approval needed`, value: OrderStatus.WAITING_APPROVAL },
    { label: $localize`Completed`, value: OrderStatus.COMPLETED }
  ];

  selectedStatus = 'ALL';

  user = {
    firstName: 'juan',
    lastName: 'perez'
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
    status: OrderStatus.COMPLETED
  }

  orders = Array(10).fill(this.order);
  //orders = Array()

}
