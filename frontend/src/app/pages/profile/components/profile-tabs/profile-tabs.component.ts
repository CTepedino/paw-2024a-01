import {Component, computed, inject, input, OnInit, output, signal} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {User} from "../../../../shared/model/user/user";
import {UserRoles} from "../../../../shared/model/user/userRoles";
import {Router} from "@angular/router";

@Component({
    selector: 'app-profile-tabs',
    standalone: true,
    imports: [CommonModule, MatTab, MatTabContent, MatTabGroup, MatTabLabel],
    templateUrl: './profile-tabs.component.html',
    styleUrl: './profile-tabs.component.scss'
})
export class ProfileTabsComponent implements OnInit {
    router = inject(Router);

    user = input.required<User>();
    ownsProfile = input.required<boolean>();

    showBoughtBooks = computed(() => this.user()?.roles?.includes(UserRoles.WRITER) ?? false);
    showPublications = signal(() => this.ownsProfile());
    showWishlist = signal(() => this.ownsProfile());

    index = { selectedIndex: 0 }

    ngOnInit(): void {
        const url = this.router.url;

        if (url.endsWith('owned')) {
            setTimeout(() => this.index.selectedIndex = this.showPublications()? 1: 0, 100);
        }
        if (url.endsWith('wishlist')) {
            setTimeout(() => this.index.selectedIndex = this.showPublications()? 2: 1, 100);
        }
    }

    onTabChange(event: any) {
        if (this.showPublications()) {
            if (event.index == 0) {
                this.router.navigate([`/profile/${this.user()?.id}/publications`]);
            }
            if (event.index == 1) {
                this.router.navigate([`/profile/${this.user()?.id}/owned`]);
            }
            if (event.index == 2) {
                this.router.navigate([`/profile/${this.user()?.id}/wishlist`]);
            }
        } else {
            if (event.index == 0) {
                this.router.navigate([`/profile/${this.user()?.id}/owned`]);

            }
            if (event.index == 1) {
                this.router.navigate([`/profile/${this.user()?.id}/wishlist`]);
            }
        }
    }
}