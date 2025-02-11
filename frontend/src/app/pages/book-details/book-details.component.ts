import {Component, input} from '@angular/core';
import {ReviewCardComponent} from "./components/review-card/review-card.component";

@Component({
  selector: 'app-book-details',
	imports: [
		ReviewCardComponent
	],
  templateUrl: './book-details.component.html',
  styleUrl: './book-details.component.scss',
})
export class BookDetailsComponent {
  id = input.required<number>();


  review = {
    reviewerInfo: {
      id: 1,
      firstName: 'Armando',
      lastName: 'Barreda',
      profilePicture: 'assets/user.jpeg'
    },
    review: "\"Dune\" es una epopeya de ciencia ficción que transporta a los lectores a un universo vasto y complejo. En un desierto inhóspito donde la política, la religión y el poder se entrelazan, seguimos a Paul Atreides en su búsqueda de dominio y supervivencia. Herbert teje una trama magistral llena de intriga y profundidad filosófica, creando un mundo fascinante que desafía las convenciones del género. Una obra maestra que cautiva desde la primera página hasta la última.",
    rating: 7,
    date: "2024-05-13T20:37:51.300944"
  }
}
