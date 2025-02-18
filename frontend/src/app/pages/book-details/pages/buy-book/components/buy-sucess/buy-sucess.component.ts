import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {
  NotificationCardComponent
} from "../../../../../../shared/components/notification-card/notification-card.component";
import {NgOptimizedImage} from "@angular/common";
import {NavButtonComponent} from "../../../../../../shared/components/nav-button/nav-button.component";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-buy-sucess',
  imports: [
    NotificationCardComponent,
    NgOptimizedImage,
    NavButtonComponent,
    TranslateModule
  ],
  templateUrl: './buy-sucess.component.html',
  styleUrl: './buy-sucess.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class BuySucessComponent {

}
