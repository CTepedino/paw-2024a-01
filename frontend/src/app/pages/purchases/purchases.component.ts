import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatGridListModule } from '@angular/material/grid-list';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatButtonModule } from '@angular/material/button';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-purchases',
  standalone: true,
  imports: [
    CommonModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatGridListModule,
    MatCheckboxModule,
    MatButtonModule,
    FormsModule
  ],
  templateUrl: './purchases.component.html',
  styleUrl: './purchases.component.scss'
})
export class PurchasesComponent {
  searchTitle = '';
  selectedStatus = 'ALL';

  statusOptions = [
    { value: 'ALL', label: 'All status' },
    { value: 'COMPLETED', label: 'Completed' },
    { value: 'REJECTED_PAYMENT', label: 'Rejected payment' },
    { value: 'WAITING_APPROVAL', label: 'Waiting approval' }
  ];

  // Datos de ejemplo
  purchases = [{
    book: {
      id: 1,
      title: 'Test Book',
      author: 'Luca Bloise',
      price: 399,
      coverUrl: 'assets/book-cover.jpg'
    },
    date: '2/10/25, 7:29PM',
    status: 'COMPLETED',
    recommended: false
  }];

  onStatusChange() {
    console.log('Status changed:', this.selectedStatus);
  }
}