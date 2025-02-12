import { Routes } from '@angular/router';
import {numericIDGuard} from "./shared/guards/numeric-id.guard";
import { HomeComponent } from './pages/home/home.component';
import {BookDetailsComponent} from "./pages/book-details/book-details.component";
import { SalesComponent } from './pages/sales/sales.component';
import {NotFoundPageComponent} from "./pages/errors/not-found-page/not-found-page.component";
import {LoginComponent} from "./pages/login/login.component";
import {ForgotPasswordComponent} from "./pages/forgot-password/forgot-password.component";


export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},
	{path: "search", component: HomeComponent},
	{path: "signup", component: HomeComponent},
	{path: "validate", component: HomeComponent},
	{path: "login", component: LoginComponent},
	{path: "forgot-password", component: ForgotPasswordComponent},
	{path: "reset-password/:id/:code", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "add-book", component: HomeComponent},
	{path: "book/:id", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/reviews", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/questions", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/my-questions", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/edit", component: HomeComponent, canActivate: [numericIDGuard]},
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
	{path: "change-password", component: HomeComponent},
	{path: "edit-profile", component: HomeComponent},
	{path: "analytics", component: HomeComponent},
	{path: "purchases", component: HomeComponent},
	{path: "sales", component: SalesComponent},
	{path: "404", component: NotFoundPageComponent},
	{path: "**", redirectTo: "404"}
];
