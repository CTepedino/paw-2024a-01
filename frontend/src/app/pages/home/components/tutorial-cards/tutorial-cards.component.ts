import { Component } from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-tutorial-cards',
	imports: [
		MatGridList,
		MatGridTile,
		TranslateModule
	],
  templateUrl: './tutorial-cards.component.html',
  styleUrl: './tutorial-cards.component.scss'
})
export class TutorialCardsComponent {

}
