import {Component, inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {AuthService} from "../../shared/services/auth.service";
import {map} from "rxjs";
import {UserRoles} from "../../shared/model/user/userRoles";
import {AskedQuestionsComponent} from "./components/asked-questions/asked-questions.component";
import {RecievedQuestionsComponent} from "./components/recieved-questions/recieved-questions.component";
import {Title} from "@angular/platform-browser";
import {TranslateModule} from "@ngx-translate/core";

@Component({
  selector: 'app-questions',
	imports: [
		MatTab,
		MatTabGroup,
		MatTabLabel,
		MatTabContent,
		AskedQuestionsComponent,
		RecievedQuestionsComponent,
		TranslateModule
	],
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.scss'
})
export class QuestionsComponent implements OnInit {
	title = inject(Title);

	router = inject(Router);
	authService = inject(AuthService);

	showTabs = false;

	index = { selectedIndex: 0 }

	constructor() {
		this.title.setTitle('Questions')


		this.authService.getLoggedUser().pipe(
			map(user => {
				this.showTabs = user?.roles?.includes(UserRoles.WRITER)!;
			})
		).subscribe();
	}

	ngOnInit(): void {
		const url = this.router.url;

		if (url.includes('received-questions')) {
			setTimeout(() => this.index.selectedIndex = 1, 100);
		}
	}

	onTabChange(event: any){
		if (event.index === 0) {
			this.router.navigate(['questions/asked-questions']);
		} else if (event.index === 1) {
			this.router.navigate(['questions/received-questions']);
		}
	}

}
