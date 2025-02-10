import { Routes } from '@angular/router';
import {numericIDGuard} from "./shared/guards/numeric-id.guard";
import { HomeComponent } from './pages/home/home.component';
import {BookDetailsComponent} from "./pages/book-details/book-details.component";

export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},
	{path: "search", component: HomeComponent},
	{path: "signup", component: HomeComponent},
	{path: "validate", component: HomeComponent},
	{path: "login", component: HomeComponent},
	{path: "change-password", component: HomeComponent},
	{path: "forgot-password", component: HomeComponent},
	{path: "reset-password/:id/:code", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "add-book", component: HomeComponent},
	{path: "book/:id", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/reviews", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/questions", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/my-questions", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "book/edit/:id", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/deal", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/buy", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "questions", component: HomeComponent},
	{path: "questions/my-questions", component: HomeComponent},
	{path: "questions/questions", component: HomeComponent},
	{path: "profile", component: HomeComponent},
	{path: "profile/:id", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/publications", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/bought-books", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/recommendations", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/wishlist", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "editProfile", component: HomeComponent},
	{path: "analytics", component: HomeComponent},
	{path: "purchases", component: HomeComponent},
	{path: "sales", component: HomeComponent},
	{path: "404", component: HomeComponent},
	{path: "**", redirectTo: "404"}
];
