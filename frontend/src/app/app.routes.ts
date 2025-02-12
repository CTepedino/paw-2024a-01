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


export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},

	{path: "signup", component: HomeComponent, canActivate: [notLoggedGuard]},
	{path: "login", component: LoginComponent, canActivate: [notLoggedGuard]},
	{path: "forgot-password", component: ForgotPasswordComponent, canActivate: [notLoggedGuard]},
	{path: "validate", component: HomeComponent, canActivate: [notLoggedGuard]},
	{path: "reset-password", component: HomeComponent, canActivate: [notLoggedGuard]},

	{path: "search", component: HomeComponent},

	{path: "add-book", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "book/:id", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/reviews", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/questions", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/my-questions", component: BookDetailsComponent, canActivate: [numericIDGuard, loggedInGuard, isNotBookWriterGuard]},
	{path: "book/:id/edit", component: HomeComponent, canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard]},
	{path: "book/:id/deal", component: HomeComponent, canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard]},
	{path: "book/:id/buy", component: HomeComponent, canActivate: [numericIDGuard, loggedInGuard, isNotBookWriterGuard]},

	{path: "questions", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "questions/asked-questions", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "questions/received-questions", component: HomeComponent, canActivate: [loggedInGuard, writerGuard] },

	{path: "profile", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "profile/:id", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/publications", component: HomeComponent, canActivate: [numericIDGuard, idIsWriterGuard]},
	{path: "profile/:id/bought-books", component: HomeComponent, canActivate: [numericIDGuard, userIdGuard, writerGuard]},
	{path: "profile/:id/recommendations", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/wishlist", component: HomeComponent, canActivate: [numericIDGuard, userIdGuard]},
	{path: "change-password", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "edit-profile", component: HomeComponent, canActivate: [loggedInGuard]},

	{path: "analytics", component: HomeComponent, canActivate: [loggedInGuard, writerGuard]},

	{path: "purchases", component: HomeComponent, canActivate: [loggedInGuard]},

	{path: "sales", component: SalesComponent, canActivate: [loggedInGuard, writerGuard]},

	{path: "404", component: NotFoundPageComponent},
	{path: "**", redirectTo: "404"}
];
