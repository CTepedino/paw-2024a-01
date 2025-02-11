import {Component, CUSTOM_ELEMENTS_SCHEMA} from '@angular/core';
import {NotificationCardComponent} from "../../../shared/components/notification-card/notification-card.component";

@Component({
  selector: 'app-not-found-page',
  imports: [
    NotificationCardComponent
  ],
  templateUrl: './not-found-page.component.html',
  styleUrl: './not-found-page.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NotFoundPageComponent {

}
