import {Routes} from '@angular/router';
import {numericIDGuard} from "./shared/guards/numeric-id.guard";
import {HomeComponent} from './pages/home/home.component';
import {BookDetailsComponent} from "./pages/book-details/book-details.component";
import {SalesComponent} from './pages/sales/sales.component';
import {NotFoundPageComponent} from "./pages/errors/not-found-page/not-found-page.component";
import {LoginComponent} from "./pages/login/login.component";
import {ForgotPasswordComponent} from "./pages/forgot-password/forgot-password.component";
import {loggedInGuard} from "./shared/guards/logged-in.guard";
import {notLoggedGuard} from "./shared/guards/not-logged.guard";
import {writerGuard} from "./shared/guards/writer.guard";
import {idIsWriterGuard} from "./shared/guards/id-is-writer.guard";
import {userIdGuard} from "./shared/guards/user-id.guard";
import {isNotBookWriterGuard} from "./shared/guards/is-not-book-writer.guard";
import {bookWriterGuard} from "./shared/guards/book-writer.guard";
import {canValidateCodeGuard} from "./shared/guards/can-validate-code.guard";
import {EmailValidationComponent} from "./pages/email-validation/email-validation.component";
import {ResetPasswordComponent} from "./pages/reset-password/reset-password.component";
import {ChangePasswordComponent} from "./pages/change-password/change-password.component";
import {AddBookComponent} from "./pages/add-book/add-book.component";

export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},

	{path: "signup", component: HomeComponent, canActivate: [notLoggedGuard]},
	{path: "login", component: LoginComponent, canActivate: [notLoggedGuard]},
	{path: "forgot-password", component: ForgotPasswordComponent, canActivate: [notLoggedGuard]},
	{path: "validate", component: EmailValidationComponent, canActivate: [canValidateCodeGuard]},
	{path: "reset-password", component: ResetPasswordComponent, canActivate: [canValidateCodeGuard]},

	{path: "search", component: HomeComponent},

	{path: "add-book", component: AddBookComponent, canActivate: [loggedInGuard]},
	{
		path: "book/:id",
		component: BookDetailsComponent,
		canActivate: [numericIDGuard],
		children: [
			{path: "reviews", component: BookDetailsComponent},
			{path: "questions", component: BookDetailsComponent},
			{path: "my-questions", component: BookDetailsComponent, canActivate: [loggedInGuard, isNotBookWriterGuard]},
			{path: "edit", component: HomeComponent, canActivate: [loggedInGuard, bookWriterGuard]},
			{path: "deal", component: HomeComponent, canActivate: [loggedInGuard, bookWriterGuard]},
			{path: "buy", component: HomeComponent, canActivate: [loggedInGuard, isNotBookWriterGuard]}
		]
	},

	{
		path: "questions",
		component: HomeComponent,
		canActivate: [loggedInGuard],
		children: [
			{path: "asked-questions", component: HomeComponent},
			{path: "received-questions", component: HomeComponent, canActivate: [writerGuard] },
		]
	},

	{path: "profile", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "change-password", component: ChangePasswordComponent, canActivate: [loggedInGuard]},
	{path: "edit-profile", component: HomeComponent, canActivate: [loggedInGuard]},
	{
		path: "profile/:id",
		component: HomeComponent,
		canActivate: [numericIDGuard],
		children: [
			{path: "publications", component: HomeComponent, canActivate: [idIsWriterGuard]},
			{path: "bought-books", component: HomeComponent, canActivate: [userIdGuard, writerGuard]},
			{path: "recommendations", component: HomeComponent},
			{path: "wishlist", component: HomeComponent, canActivate: [userIdGuard]}
		]
	},

	{path: "analytics", component: HomeComponent, canActivate: [loggedInGuard, writerGuard]},

	{path: "purchases", component: HomeComponent, canActivate: [loggedInGuard]},

	{path: "sales", component: SalesComponent, canActivate: [loggedInGuard, writerGuard]},

	{path: "404", component: NotFoundPageComponent},
	{path: "**", redirectTo: "404"}
];
