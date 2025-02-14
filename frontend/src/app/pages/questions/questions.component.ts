import {Component, inject, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {MatTab, MatTabContent, MatTabGroup, MatTabLabel} from "@angular/material/tabs";
import {AuthService} from "../../shared/services/auth.service";
import {BehaviorSubject, map} from "rxjs";
import {UserRoles} from "../../shared/model/user/userRoles";
import {AsyncPipe} from "@angular/common";
import {AskedQuestionsComponent} from "./components/asked-questions/asked-questions.component";
import {RecievedQuestionsComponent} from "./components/recieved-questions/recieved-questions.component";

@Component({
  selector: 'app-questions',
	imports: [
		MatTab,
		MatTabGroup,
		MatTabLabel,
		AsyncPipe,
		MatTabContent,
		AskedQuestionsComponent,
		RecievedQuestionsComponent
	],
  templateUrl: './questions.component.html',
  styleUrl: './questions.component.scss'
})
export class QuestionsComponent implements OnInit {

	route = inject(ActivatedRoute);
	router = inject(Router);
	authService = inject(AuthService);

	showTabs = false;

	private selectedIndexSubject = new BehaviorSubject<number>(0);
	selectedIndex$ = this.selectedIndexSubject.asObservable();

	constructor() {
		this.authService.getLoggedUser().pipe(
			map(user => {
				this.showTabs = user?.roles?.includes(UserRoles.WRITER)!;
			})
		).subscribe();
	}

	ngOnInit(): void {
		const urlSegments = this.route.snapshot.url;
		const lastSegment = urlSegments[urlSegments.length - 1]?.path;

		if (lastSegment === 'received-questions') {
			this.selectedIndexSubject.next(1);
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
