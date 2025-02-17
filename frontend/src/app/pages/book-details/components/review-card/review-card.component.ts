import {Component, input} from '@angular/core';
import {MatCard, MatCardContent, MatCardHeader, MatCardImage} from "@angular/material/card";
import {RouterLink} from "@angular/router";
import {ReviewWithInfo} from "../../../../shared/model/review/reviewWithInfo";
import {DatePipe} from "@angular/common";
import {StarRatingComponent} from "../../../../shared/components/star-rating/star-rating.component";

@Component({
    selector: 'app-review-card',
    imports: [
        MatCard,
        MatCardContent,
        MatCardHeader,
        MatCardImage,
        RouterLink,
        DatePipe,
        StarRatingComponent
    ],
    templateUrl: './review-card.component.html',
    standalone: true,
    styleUrl: './review-card.component.scss'
})
export class ReviewCardComponent {
  review = input.required<ReviewWithInfo>();
}
