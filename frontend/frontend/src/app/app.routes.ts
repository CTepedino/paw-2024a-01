import { Routes } from '@angular/router';
import {AppComponent} from "./app.component";

export const routes: Routes = [
	{path: "", component: AppComponent},
	{path: "search", component: AppComponent},
	{path: "signup", component: AppComponent},
	{path: "validate", component: AppComponent},
	{path: "login", component: AppComponent},
	{path: "change-password", component: AppComponent},
	{path: "forgot-password", component: AppComponent},
	{path: "reset-password/{id:\\d+}/{code:\\d+}", component: AppComponent},
	
];
