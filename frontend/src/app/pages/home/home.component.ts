import { Component } from '@angular/core';
import {MatGridListModule} from '@angular/material/grid-list';
import {TutorialCardsComponent} from "./components/tutorial-cards/tutorial-cards.component";

@Component({
  selector: 'app-home',
  imports: [MatGridListModule, TutorialCardsComponent],
  templateUrl: './home.component.html',
  styleUrl: './home.component.scss'
})
export class HomeComponent {

}
