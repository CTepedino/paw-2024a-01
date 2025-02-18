import {Component, inject, input} from '@angular/core';
import {FormsModule} from "@angular/forms";

import {MatDialog} from "@angular/material/dialog";
import {tap} from "rxjs";
import {CancelButtonComponent} from "../../../../shared/components/cancel-button/cancel-button.component";
import {MatIcon} from "@angular/material/icon";
import {ActionButtonComponent} from "../../../../shared/components/action-button/action-button.component";
import {CurrencyPipe, DatePipe, NgOptimizedImage} from "@angular/common";
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {BookService} from "../../../../shared/services/book.service";
import {OrderService} from "../../../../shared/services/order.service";
import {OrderWithData} from "../../../../shared/model/order/orderWithData";
import {OrderStatus} from "../../../../shared/model/order/orderStatus";
import {RejectionPopupComponent} from "../rejection-popup/rejection-popup.component";
import {ReuploadPopupComponent} from "../reupload-popup/reupload-popup.component";
import {TranslateModule} from "@ngx-translate/core";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-purchases-order-card',
	imports: [
		FormsModule,
		ActionButtonComponent,
		CurrencyPipe,
		DatePipe,
		MatGridList,
		MatGridTile,
		MatIcon,
		CancelButtonComponent,
		TranslateModule,
		NgOptimizedImage,
		RouterLink
	],
  templateUrl: './purchases-order-card.component.html',
  styleUrl: './purchases-order-card.component.scss'
})
export class PurchasesOrderCardComponent {
  readonly dialog = inject(MatDialog);
  bookService = inject(BookService);
  orderService = inject(OrderService);

  order = input.required<OrderWithData>();

  openRejectionDialog(): void {
    const rejectionDialogue = this.dialog.open(RejectionPopupComponent, {
      width: '99%',
      height: '175px',
      data: {reason: this.order().rejectedReason }
    });
  }

  openReuploadDialog(): void {
    const reuploadDiagloue = this.dialog.open(ReuploadPopupComponent, {
      width: '99%',
      height: '260px',
    });

    reuploadDiagloue.afterClosed().subscribe(result => {
      if (result.file){
        this.orderService.putReceipt(this.order().self!, result.file).pipe(tap(() => {
          this.order().status = OrderStatus.WAITING_APPROVAL;
        })).subscribe();
      }
    })
  }



  protected readonly OrderStatus = OrderStatus;
}
