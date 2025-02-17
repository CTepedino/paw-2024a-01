import {Component} from '@angular/core';
import {MatTab, MatTabGroup} from "@angular/material/tabs";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-tab',
  imports: [
    MatTabGroup,
    MatTab,
    TranslateModule
  ],
  templateUrl: './tab.component.html',
  styleUrl: './tab.component.scss'
})
export class TabComponent {

}
