import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute} from "@angular/router";
import {ProfileHeaderComponent} from "./components/profile-header/profile-header.component";
import {Observable} from "rxjs";
import {ProfileDisplayInfo, UserProfileService} from "./store/user-profile.service";
import {ProfileTabsComponent} from "./components/profile-tabs/profile-tabs.component";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-profile',
  standalone: true,
	imports: [
		CommonModule,
		ProfileHeaderComponent,
		ProfileTabsComponent
	],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
	title = inject(Title);
	route = inject(ActivatedRoute);
	userProfileService = inject(UserProfileService);

	index = { selectedIndex: 0 }

	userId: number;
	displayInfo$: Observable<ProfileDisplayInfo>;

	constructor() {
		this.title.setTitle('Profile');

		this.userId = this.route.snapshot.params['id'];
		this.displayInfo$ = this.userProfileService.getUser(this.userId);
	}
}