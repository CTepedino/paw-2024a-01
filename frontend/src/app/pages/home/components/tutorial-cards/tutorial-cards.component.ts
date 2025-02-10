import { Component } from '@angular/core';
import {MatGridList, MatGridTile} from "@angular/material/grid-list";

@Component({
  selector: 'app-tutorial-cards',
	imports: [
		MatGridList,
		MatGridTile
	],
  templateUrl: './tutorial-cards.component.html',
  styleUrl: './tutorial-cards.component.scss'
})
export class TutorialCardsComponent {

}
