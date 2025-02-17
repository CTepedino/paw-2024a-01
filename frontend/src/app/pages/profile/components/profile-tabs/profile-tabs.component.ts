import {Component, computed, inject, input, OnInit, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {User} from "../../../../shared/model/user/user";
import {UserRoles} from "../../../../shared/model/user/userRoles";
import {Router} from "@angular/router";
import {WishlistTabComponent} from "../wishlist-tab/wishlist-tab.component";
import {RecommendationsTabComponent} from "../recommendations-tab/recommendations-tab.component";
import {PublicationsTabComponent} from "../publications-tab/publications-tab.component";
import {BoughtBooksTabComponent} from "../bought-books-tab/bought-books-tab.component";

@Component({
    selector: 'app-profile-tabs',
    standalone: true,
    imports: [CommonModule, MatTab, MatTabContent, MatTabGroup, MatTabLabel, WishlistTabComponent, RecommendationsTabComponent, PublicationsTabComponent, BoughtBooksTabComponent],
    templateUrl: './profile-tabs.component.html',
    styleUrl: './profile-tabs.component.scss'
})
export class ProfileTabsComponent implements OnInit {
    router = inject(Router);

    user = input.required<User>();
    ownsProfile = input.required<boolean>();

    showPublications = computed(() => this.user()?.roles?.includes(UserRoles.WRITER) ?? false);
    showBoughtBooks = computed(() => this.ownsProfile());
    showWishlist = computed(() => this.ownsProfile());

    index = { selectedIndex: 0 }

    setup = true;

    ngOnInit(): void {
        const url = this.router.url;

        setTimeout(() => this.setTab(url), 100);
    }

    setTab(url: string){
        if (url.includes('owned')) {
            this.index.selectedIndex = this.showPublications()? 1: 0;
        }
        if (url.includes('wishlist')) {
            this.index.selectedIndex = this.showPublications()? 2: 1;
        }
        setTimeout(() => this.setup = false, 100)
    }

    onTabChange(event: any) {
        if (this.showPublications()) {
            if (event.index == 0) {
                this.navigate('publications');
            }
            if (event.index == 1) {
                this.navigate('owned');
            }
            if (event.index == 2) {
                this.navigate('wishlist');
            }
        } else {
            if (event.index == 0) {
                this.navigate('owned');

            }
            if (event.index == 1) {
                this.navigate('wishlist');
            }
        }
    }

    navigate(tab: string){
        if (this.setup){
            this.router.navigate([`/profile/${this.user()?.id}/${tab}`], {queryParamsHandling: 'merge'})
        } else {
            this.router.navigate([`/profile/${this.user()?.id}/${tab}`])
        }
    }
}