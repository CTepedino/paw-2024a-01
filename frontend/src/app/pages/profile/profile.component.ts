import {Component, inject, input, OnInit} from '@angular/core';
import {CommonModule} from '@angular/common';
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {ActivatedRoute, Router} from "@angular/router";
import {AuthService} from "../../shared/services/auth.service";
import {ProfileHeaderComponent} from "./components/profile-header/profile-header.component";
import {UserRoles} from "../../shared/model/user/userRoles";
import {User} from "../../shared/model/user/user";
import {WriterCategory} from "../../shared/model/user/writerCategory";
import {Observable} from "rxjs";
import {UserService} from "../../shared/services/user.service";
import {ProfileDisplayInfo, UserProfileService} from "./store/user-profile.service";

@Component({
  selector: 'app-profile',
  standalone: true,
	imports: [
		CommonModule,
		MatTabGroup,
		MatTab,
		MatTabContent,
		MatTabLabel,
		ProfileHeaderComponent
	],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent implements OnInit{
	router = inject(Router);
	route = inject(ActivatedRoute);
	userProfileService = inject(UserProfileService);

	index = { selectedIndex: 0 }

	userId: number;
	displayInfo$: Observable<ProfileDisplayInfo>;

	constructor() {
		this.userId = this.route.snapshot.params['id'];
		this.displayInfo$ = this.userProfileService.getUser(this.userId);
	}

	ngOnInit(): void {
		const url = this.router.url;

		if (url.endsWith('received-questions')) {
			setTimeout(() => this.index.selectedIndex = 1, 100);
		}
	}




  selectedTab = 'publications';

	onTabChange(event: any){
/*		if (event.index === 1) {
			this.router.navigate([`profile/${userId}/bought-books`]);
		} else if (event.index === 2){
			this.router.navigate([`profile/${userId}/recommendations`]);
		} else if (event.index === 2){
			this.router.navigate([`profile/${userId}/wishlist`]);
		}		if (event.index === 0) {
			this.router.navigate([`profile/${userId}/publications`]);
		}*/
	}

}