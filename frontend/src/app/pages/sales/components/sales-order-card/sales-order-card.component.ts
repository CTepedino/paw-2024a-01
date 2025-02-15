import {Component, inject, input} from '@angular/core';
import {OrderStatus} from "../../../../shared/model/order/orderStatus";
import {MatGridListModule} from "@angular/material/grid-list";
import {CurrencyPipe, DatePipe} from "@angular/common";
import {MatButtonModule} from "@angular/material/button";
import {MatDialog} from "@angular/material/dialog";
import {AcceptPopupComponent} from "../accept-popup/accept-popup.component";
import {DeclinePopupComponent} from "../decline-popup/decline-popup.component";
import {OrderWithData} from "../../../../shared/model/order/orderWithData";
import {OrderService} from "../../../../shared/services/order.service";
import {tap} from "rxjs";

@Component({
  selector: 'app-sales-order-card',
  imports: [MatGridListModule, CurrencyPipe, DatePipe, MatButtonModule],
  templateUrl: './sales-order-card.component.html',
  styleUrl: './sales-order-card.component.scss'
})
export class SalesOrderCardComponent {
  readonly dialog = inject(MatDialog);
  orderService = inject(OrderService);

  order = input.required<OrderWithData>();


  openAcceptDialog(): void {
    const acceptDialogue = this.dialog.open(AcceptPopupComponent, {
      width: '99%',
      height: '175px',
    });

    acceptDialogue.afterClosed().subscribe(result => {
      if (result.accept){
        this.orderService.patchOrder(this.order().self!, null).pipe(tap(() => {
          this.order().status = OrderStatus.COMPLETED;
        })).subscribe();
      }
    })
  }

  openDeclineDialog(): void {
    const declineDialogue = this.dialog.open(DeclinePopupComponent, {
      width: '99%',
      height: '260px',
    });

    declineDialogue.afterClosed().subscribe(result => {
      if (result.reason){
        this.orderService.patchOrder(this.order().self!, result.reason).pipe(tap(() => {
          this.order().status = OrderStatus.REJECTED_PAYMENT;
          this.order().rejectedReason = result.reason;
        })).subscribe();
      }
    })
  }


  protected readonly OrderStatus = OrderStatus;
}
