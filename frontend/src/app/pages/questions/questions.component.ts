import {Component, inject, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {AuthService} from "../../shared/services/auth.service";
import {map} from "rxjs";
import {UserRoles} from "../../shared/model/user/userRoles";
import {AskedQuestionsComponent} from "./components/asked-questions/asked-questions.component";
import {RecievedQuestionsComponent} from "./components/recieved-questions/recieved-questions.component";
import {Title} from "@angular/platform-browser";

@Component({
  selector: 'app-questions',
	imports: [
		MatTab,
		MatTabGroup,
		MatTabLabel,
		MatTabContent,
		AskedQuestionsComponent,
		RecievedQuestionsComponent
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

	setup = true;

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

		this.setTab(url);
	}


	setTab(url: string){
		if (url.includes('received-questions')) {
			this.index.selectedIndex = 1;
		}
		setTimeout(() => this.setup = false, 100)
	}


	onTabChange(event: any){
		if (event.index === 0) {
			this.navigate('asked-questions');
		} else if (event.index === 1) {
			this.navigate('received-questions');
		}
	}

	navigate(tab: string){
		this.router.navigate([`/questions//${tab}`], {queryParamsHandling: this.setup? 'merge' : undefined})
	}
}
