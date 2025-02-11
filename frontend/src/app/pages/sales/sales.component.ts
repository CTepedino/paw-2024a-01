import { Component } from '@angular/core';
import {MatGridListModule} from "@angular/material/grid-list";
import {MatFormFieldModule, MatLabel} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule} from "@angular/forms";

export enum OrderStatus {
  All = 'All status',
  Rejected = 'Rejected payment',
  Approval = 'Approval needed',
  Completed = 'Completed',
}

@Component({
  selector: 'app-sales',
  imports: [MatGridListModule, MatFormFieldModule, MatLabel, MatInputModule, MatSelectModule, FormsModule],
  templateUrl: './sales.component.html',
  styleUrl: './sales.component.scss',
})
export class SalesComponent {
  selectedStatus: OrderStatus = OrderStatus.All;
  statusValues = Object.values(OrderStatus);
}
