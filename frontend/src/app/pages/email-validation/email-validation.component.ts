import {Component, CUSTOM_ELEMENTS_SCHEMA, inject} from '@angular/core';
import {
  ActionNotificationCardComponent
} from "../../shared/components/action-notification-card/action-notification-card.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";


@Component({
  selector: 'app-email-validation',
  imports: [
    ActionNotificationCardComponent,
    TranslateModule
  ],
  templateUrl: './email-validation.component.html',
  styleUrl: './email-validation.component.scss',
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class EmailValidationComponent {
  title = inject(Title);

  constructor(private translate: TranslateService) {
    this.translate.get('PAGE_VALIDATE_EMAIL').subscribe(translatedTitle => {
      this.title.setTitle(translatedTitle);
    });

  }
}
