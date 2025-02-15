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
import {SearchComponent} from "./pages/search/search.component";
import {ChangePasswordComponent} from "./pages/change-password/change-password.component";
import {AddBookComponent} from "./pages/add-book/add-book.component";
import {EditBookComponent} from "./pages/edit-book/edit-book.component";
import {BuyBookComponent} from "./pages/book-details/pages/buy-book/buy-book.component";
import {canBuyBookGuard} from "./shared/guards/can-buy-book.guard";
import { ProfileComponent } from './pages/profile/profile.component';
import {DealComponent} from "./pages/book-details/pages/deal/deal.component";
import {QuestionsComponent} from "./pages/questions/questions.component";
import {AskedQuestionsComponent} from "./pages/questions/components/asked-questions/asked-questions.component";
import {RecievedQuestionsComponent} from "./pages/questions/components/recieved-questions/recieved-questions.component";
import {EditProfileComponent} from "./pages/edit-profile/edit-profile.component";
import {SignupComponent} from "./pages/signup/signup.component";
import {PurchasesComponent} from "./pages/purchases/purchases.component";
import {AnalyticsComponent} from "./pages/analytics/analytics.component";



export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},

	//{path: "signup", component: HomeComponent, canActivate: [notLoggedGuard]},
	{path: "login", component: LoginComponent, canActivate: [notLoggedGuard]},
	{path: "forgot-password", component: ForgotPasswordComponent, canActivate: [notLoggedGuard]},
	{path: "validate", component: EmailValidationComponent, canActivate: [canValidateCodeGuard]},
	{path: "reset-password", component: ResetPasswordComponent, canActivate: [canValidateCodeGuard]},

	{path: "search", component: SearchComponent},

	{path: "add-book", component: AddBookComponent, canActivate: [loggedInGuard]},
	{path: "book/:id", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/reviews", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/questions", component: BookDetailsComponent, canActivate: [numericIDGuard]},
	{path: "book/:id/my-questions", component: BookDetailsComponent, canActivate: [numericIDGuard, loggedInGuard, isNotBookWriterGuard]},
	{path: "book/:id/edit", component: EditBookComponent, canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard]},
	{path: "book/:id/deal", component: DealComponent, canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard]},
	{path: "book/:id/buy", component: BuyBookComponent, canActivate: [numericIDGuard, loggedInGuard, isNotBookWriterGuard, canBuyBookGuard]},

	{path: "questions", component: QuestionsComponent, canActivate: [loggedInGuard], children: [
			{path: '', redirectTo: 'asked-questions', pathMatch: 'full' },
			{path: "asked-questions", component: AskedQuestionsComponent, canActivate: [loggedInGuard]},
			{path: "received-questions", component: RecievedQuestionsComponent, canActivate: [loggedInGuard, writerGuard] },
	]},


	{path: "profile", component: HomeComponent, canActivate: [loggedInGuard]},
	{path: "profile/:id", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/publications", component: HomeComponent, canActivate: [numericIDGuard, idIsWriterGuard]},
	{path: "profile/:id/bought-books", component: HomeComponent, canActivate: [numericIDGuard, userIdGuard, writerGuard]},
	{path: "profile/:id/recommendations", component: HomeComponent, canActivate: [numericIDGuard]},
	{path: "profile/:id/wishlist", component: HomeComponent, canActivate: [numericIDGuard, userIdGuard]},
	{path: "change-password", component: ChangePasswordComponent, canActivate: [loggedInGuard]},
	//{path: "edit-profile", component: HomeComponent, canActivate: [loggedInGuard]},

	{path: "analytics", component: AnalyticsComponent, canActivate: [loggedInGuard, writerGuard]},

	//{path: "purchases", component: HomeComponent, canActivate: [loggedInGuard]},
	{
		path: "purchases", component: PurchasesComponent
	},
	{path: "sales", component: SalesComponent, canActivate: [loggedInGuard, writerGuard]},
	{path: "my-profile", component: ProfileComponent},
	{path: "edit-profile", component: EditProfileComponent},
	{path: "signup", component: SignupComponent, canActivate: [notLoggedGuard]},

	{path: "404", component: NotFoundPageComponent},
	{path: "**", redirectTo: "404"}
];
