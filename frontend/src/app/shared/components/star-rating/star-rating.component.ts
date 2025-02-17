import {Component, computed, input} from '@angular/core';
import {MatIcon} from "@angular/material/icon";

@Component({
    selector: 'app-star-rating',
    imports: [
        MatIcon
    ],
    templateUrl: './star-rating.component.html',
    standalone: true,
    styleUrl: './star-rating.component.scss'
})
export class StarRatingComponent {
  rating = input.required<number>();

  starCount = Array(5).fill('');

  fullStars = computed<number>(() => Math.min(Math.floor(this.rating()/2), 5));
  halfStar = computed<boolean>(() => this.rating()%2!==0 && this.fullStars() < 5);

}
