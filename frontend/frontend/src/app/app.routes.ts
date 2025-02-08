import { Routes } from '@angular/router';
import {AppComponent} from "./app.component";
import {HomeComponent} from "./pages/home/home.component"
import {numericIDGuard} from "./shared/guards/numeric-id.guard";

export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},
	{path: "search", component: AppComponent},
	{path: "signup", component: AppComponent},
	{path: "validate", component: AppComponent},
	{path: "login", component: AppComponent},
	{path: "change-password", component: AppComponent},
	{path: "forgot-password", component: AppComponent},
	{path: "reset-password/:id/:code", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "add-book", component: AppComponent},
	{path: "book/:id", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/reviews", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/questions", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/my-questions", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "book/edit/:id", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/deal", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/buy", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "questions", component: AppComponent},
	{path: "questions/my-questions", component: AppComponent},
	{path: "questions/questions", component: AppComponent},
	{path: "profile", component: AppComponent},
	{path: "profile/:id", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/publications", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/bought-books", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/recommendations", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/wishlist", component: AppComponent, canActivate: [numericIDGuard]},
	{path: "editProfile", component: AppComponent},
	{path: "analytics", component: AppComponent},
	{path: "purchases", component: AppComponent},
	{path: "sales", component: AppComponent},
	//{path: "**", component: AppComponent}
];
