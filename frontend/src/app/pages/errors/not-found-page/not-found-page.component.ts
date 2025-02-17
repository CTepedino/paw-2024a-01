import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {
  ActionNotificationCardComponent
} from "../../../shared/components/action-notification-card/action-notification-card.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-not-found-page',
  imports: [
    ActionNotificationCardComponent,
    TranslateModule
  ],
  templateUrl: './not-found-page.component.html',
  styleUrl: './not-found-page.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class NotFoundPageComponent {
  title = inject(Title);

  constructor(private translate: TranslateService) {
    this.translate.get('PAGE_NOT_FOUND_BROWSER').subscribe(translatedTitle => {
      this.title.setTitle(translatedTitle);
    });

  }
}
