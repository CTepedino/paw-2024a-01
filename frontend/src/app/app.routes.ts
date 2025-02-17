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
import {BuyBookComponent} from "./pages/book-details/pages/buy-book/buy-book.component";
import {canBuyBookGuard} from "./shared/guards/can-buy-book.guard";
import {ProfileComponent} from './pages/profile/profile.component';
import {DealComponent} from "./pages/book-details/pages/deal/deal.component";
import {QuestionsComponent} from "./pages/questions/questions.component";
import {AskedQuestionsComponent} from "./pages/questions/components/asked-questions/asked-questions.component";
import {RecievedQuestionsComponent} from "./pages/questions/components/recieved-questions/recieved-questions.component";
import {EditProfileComponent} from "./pages/edit-profile/edit-profile.component";
import {SignupComponent} from "./pages/signup/signup.component";
import {PurchasesComponent} from "./pages/purchases/purchases.component";
import {userExistsGuard} from "./shared/guards/user-exists.guard";
import {bookExistsGuard} from "./shared/guards/book-exists.guard";
import {SearchComponent} from "./pages/search/search.component";
import {AnalyticsComponent} from "./pages/analytics/analytics.component";
import {profileRedirectGuard} from "./shared/guards/profile-redirect.guard";
import {BoughtBooksTabComponent} from "./pages/profile/components/bought-books-tab/bought-books-tab.component";
import {WishlistTabComponent} from "./pages/profile/components/wishlist-tab/wishlist-tab.component";
import {PublicationsTabComponent} from "./pages/profile/components/publications-tab/publications-tab.component";
import {EditBookComponent} from "./pages/book-details/pages/edit-book/edit-book.component";

export const routes: Routes = [
	{path: "", component: HomeComponent, pathMatch: "full"},
	{path: "search", component: SearchComponent},

	{path: "signup", component: SignupComponent, canActivate: [notLoggedGuard]},
	{path: "login", component: LoginComponent, canActivate: [notLoggedGuard]},
	{path: "forgot-password", component: ForgotPasswordComponent, canActivate: [notLoggedGuard]},
	{path: "validate", component: EmailValidationComponent, canActivate: [canValidateCodeGuard]},
	{path: "reset-password", component: ResetPasswordComponent, canActivate: [canValidateCodeGuard]},

	{path: "add-book", component: AddBookComponent, canActivate: [loggedInGuard]},
	{path: "book/:id", component: BookDetailsComponent, canActivate: [numericIDGuard, bookExistsGuard], children: [
			{path: "reviews", component: BookDetailsComponent},
			{path: "questions", component: BookDetailsComponent},
			{path: "my-questions", component: BookDetailsComponent, canActivate: [loggedInGuard, isNotBookWriterGuard]},
	]},
	{path: "book/:id/edit", component: EditBookComponent, canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard, bookExistsGuard]},
	{path: "book/:id/deal", component: DealComponent, canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard, bookExistsGuard]},
	{path: "book/:id/buy", component: BuyBookComponent, canActivate: [numericIDGuard, loggedInGuard, isNotBookWriterGuard, canBuyBookGuard, bookExistsGuard]},

	{path: "questions", component: QuestionsComponent, canActivate: [loggedInGuard], children: [
			{path: "asked-questions", component: AskedQuestionsComponent, canActivate: [loggedInGuard]},
			{path: "received-questions", component: RecievedQuestionsComponent, canActivate: [loggedInGuard, writerGuard] },
	]},

	{path: "profile", component: ProfileComponent , canActivate: [loggedInGuard, profileRedirectGuard]},
	{path: "profile/:id", component: ProfileComponent, canActivate: [numericIDGuard, userExistsGuard], children: [
			{path: "owned", component: BoughtBooksTabComponent},
			{path: "wishlist", component: WishlistTabComponent, canActivate: [userIdGuard]},
			{path: "publications", component: PublicationsTabComponent, canActivate: [idIsWriterGuard]},
	]},

	{path: "change-password", component: ChangePasswordComponent, canActivate: [loggedInGuard]},
	{path: "edit-profile", component: EditProfileComponent, canActivate: [loggedInGuard]},

	{path: "analytics", component: AnalyticsComponent, canActivate: [loggedInGuard, writerGuard]},

	{path: "purchases", component: PurchasesComponent, canActivate: [loggedInGuard]},
	{path: "sales", component: SalesComponent, canActivate: [loggedInGuard, writerGuard]},

	{path: "404", component: NotFoundPageComponent},
	{path: "**", redirectTo: "404"}
];
