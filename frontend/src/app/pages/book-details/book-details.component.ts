import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatIconModule } from '@angular/material/icon';
import { RouterModule } from '@angular/router';
import { StarRatingComponent } from '../../shared/components/star-rating/star-rating.component';
import { BookBadgeComponent } from '../../shared/components/book-badge/book-badge.component';
import { ActionButtonComponent } from '../../shared/components/action-button/action-button.component';
import { SalesCategory } from '../../shared/model/book/salesCategory';

@Component({
    selector: 'app-book-details',
    standalone: true,
    imports: [
        CommonModule,
        MatIconModule,
        RouterModule,
        StarRatingComponent,
        BookBadgeComponent,
        ActionButtonComponent
    ],
    templateUrl: './book-details.component.html',
    styleUrl: './book-details.component.scss'
})
export class BookDetailsComponent implements OnInit {
    book = {
        id: 1,
        coverUrl: 'assets/images/dune.jpg',
        title: 'Dune',
        author: 'Don Nadie',
        authorId: 1,
        writerCategory: 'BRONZE',
        rating: 5,
        reviewCount: 2,
        originalPrice: 31199,
        currentPrice: 15000,
        discountPercentage: 52,
        publishDate: '2024-05-13',
        genre: 'Ciencia ficción',
        pageCount: 784,
        ageRestriction: 8,
        salesCategory: SalesCategory.POPULAR,
        description: 'Arrakis: un planeta desértico donde el agua es el bien más preciado y, donde llorar a los muertos es el símbolo de máxima prodigalidad. Paul Atreides: un adolescente marcado por un destino singular, dotado de extraños poderes y, abocado a convertirse en dictador, mesías y mártir...',
        previewUrl: 'url-to-preview-pdf',
        // Estados
        isAuthor: false,
        ownsBook: false,
        isWishlisted: false,
        hasReview: false
    };

    // Recomendaciones
    recommendations = [
        {
            id: 1,
            title: 'El sueño de la vaca',
            author: 'Federico Madero',
            price: 143,
            coverUrl: 'assets/covers/book1.jpg'
        },
        {
            id: 2,
            title: 'Los Mitos De Cthulhu',
            author: 'Don Nadie',
            price: 29500,
            coverUrl: 'assets/covers/book2.jpg'
        },
        {
            id: 3,
            title: 'Fundación E Imperio',
            author: 'Don Nadie',
            price: 16900,
            coverUrl: 'assets/covers/book3.jpg'
        }
    ];

    // Tabs
    selectedTab: 'reviews' | 'questions' | 'myQuestions' = 'reviews';
    reviewCount = 2;
    questionCount = 1;
    myQuestionCount = 0;

    constructor() {}

    ngOnInit() {
        // Aquí irían las llamadas a los servicios para obtener los datos reales
    }

    buyBook() {
        console.log('Comprando libro...');
    }

    toggleWishlist() {
        this.book.isWishlisted = !this.book.isWishlisted;
        // Aquí iría la llamada al servicio
    }

    readBook() {
        window.open(`/book/file/${this.book.id}`, '_blank');
    }

    goToPurchases() {
        // Implementar navegación
    }

    openReviewModal() {
        // Implementar apertura de modal
    }

    protected readonly SalesCategory = SalesCategory;
}