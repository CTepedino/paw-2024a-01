import {Component, inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {ActivatedRoute} from "@angular/router";
import {ProfileHeaderComponent} from "./components/profile-header/profile-header.component";
import {Observable} from "rxjs";
import {ProfileDisplayInfo, UserProfileService} from "./store/user-profile.service";
import {ProfileTabsComponent} from "./components/profile-tabs/profile-tabs.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule, TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-profile',
  standalone: true,
	imports: [
		CommonModule,
		ProfileHeaderComponent,
		ProfileTabsComponent,
		TranslateModule
	],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.scss'
})
export class ProfileComponent {
	title = inject(Title);
	route = inject(ActivatedRoute);
	userProfileService = inject(UserProfileService);

	index = { selectedIndex: 0 }

	userId: number = 0;
	displayInfo$: Observable<ProfileDisplayInfo> | null = null;

	constructor(private translate: TranslateService) {
		this.translate.get('PROFILE_BROWSER').subscribe(translatedTitle => {
			this.title.setTitle(translatedTitle);
		});


		this.route.params.subscribe(params => {
			this.userId = params['id'];
			this.displayInfo$ = this.userProfileService.getUser(this.userId);
		});

	}
}