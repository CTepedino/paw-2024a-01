import {Routes} from '@angular/router';
import {numericIDGuard} from "./shared/guards/numeric-id.guard";
import {loggedInGuard} from "./shared/guards/logged-in.guard";
import {notLoggedGuard} from "./shared/guards/not-logged.guard";
import {writerGuard} from "./shared/guards/writer.guard";
import {idIsWriterGuard} from "./shared/guards/id-is-writer.guard";
import {userIdGuard} from "./shared/guards/user-id.guard";
import {isNotBookWriterGuard} from "./shared/guards/is-not-book-writer.guard";
import {bookWriterGuard} from "./shared/guards/book-writer.guard";
import {canValidateCodeGuard} from "./shared/guards/can-validate-code.guard";
import {canBuyBookGuard} from "./shared/guards/can-buy-book.guard";
import {userExistsGuard} from "./shared/guards/user-exists.guard";
import {bookExistsGuard} from "./shared/guards/book-exists.guard";
import {profileRedirectGuard} from "./shared/guards/profile-redirect.guard";

export const routes: Routes = [

	{path: "", loadComponent: () => import('./pages/home/home.component').then(m => m.HomeComponent), pathMatch: "full"},

	{path: "search",  loadComponent: () => import('./pages/search/search.component').then(m => m.SearchComponent) },

	{ path: "signup", loadComponent: () => import('./pages/signup/signup.component').then(m => m.SignupComponent), canActivate: [notLoggedGuard] },
	{ path: "login", loadComponent: () => import('./pages/login/login.component').then(m => m.LoginComponent), canActivate: [notLoggedGuard] },
	{ path: "forgot-password", loadComponent: () => import('./pages/forgot-password/forgot-password.component').then(m => m.ForgotPasswordComponent), canActivate: [notLoggedGuard] },
	{ path: "validate", loadComponent: () => import('./pages/email-validation/email-validation.component').then(m => m.EmailValidationComponent), canActivate: [canValidateCodeGuard] },
	{ path: "reset-password", loadComponent: () => import('./pages/reset-password/reset-password.component').then(m => m.ResetPasswordComponent), canActivate: [canValidateCodeGuard] },

	{ path: "add-book", loadComponent: () => import('./pages/add-book/add-book.component').then(m => m.AddBookComponent), canActivate: [loggedInGuard] },

	{ path: "book/:id",
		loadComponent: () => import('./pages/book-details/book-details.component').then(m => m.BookDetailsComponent),
		canActivate: [numericIDGuard, bookExistsGuard],
		children: [
			{ path: "reviews", loadComponent: () => import('./pages/book-details/book-details.component').then(m => m.BookDetailsComponent) },
			{ path: "questions", loadComponent: () => import('./pages/book-details/book-details.component').then(m => m.BookDetailsComponent) },
			{ path: "my-questions", loadComponent: () => import('./pages/book-details/book-details.component').then(m => m.BookDetailsComponent), canActivate: [loggedInGuard, isNotBookWriterGuard] },
		]
	},

	{ path: "book/:id/edit", loadComponent: () => import('./pages/book-details/pages/edit-book/edit-book.component').then(m => m.EditBookComponent), canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard, bookExistsGuard] },
	{ path: "book/:id/deal", loadComponent: () => import('./pages/book-details/pages/deal/deal.component').then(m => m.DealComponent), canActivate: [numericIDGuard, loggedInGuard, bookWriterGuard, bookExistsGuard] },
	{ path: "book/:id/buy", loadComponent: () => import('./pages/book-details/pages/buy-book/buy-book.component').then(m => m.BuyBookComponent), canActivate: [numericIDGuard, loggedInGuard, isNotBookWriterGuard, canBuyBookGuard, bookExistsGuard] },

	{ path: "questions",
		loadComponent: () => import('./pages/questions/questions.component').then(m => m.QuestionsComponent),
		canActivate: [loggedInGuard],
		children: [
			{ path: "asked-questions", loadComponent: () => import('./pages/questions/components/asked-questions/asked-questions.component').then(m => m.AskedQuestionsComponent), canActivate: [loggedInGuard] },
			{ path: "received-questions", loadComponent: () => import('./pages/questions/components/recieved-questions/recieved-questions.component').then(m => m.RecievedQuestionsComponent), canActivate: [loggedInGuard, writerGuard] },
		]
	},

	{ path: "profile", loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent), canActivate: [loggedInGuard, profileRedirectGuard] },

	{ path: "profile/:id",
		loadComponent: () => import('./pages/profile/profile.component').then(m => m.ProfileComponent),
		canActivate: [numericIDGuard, userExistsGuard],
		children: [
			{ path: "owned", loadComponent: () => import('./pages/profile/components/bought-books-tab/bought-books-tab.component').then(m => m.BoughtBooksTabComponent) },
			{ path: "wishlist", loadComponent: () => import('./pages/profile/components/wishlist-tab/wishlist-tab.component').then(m => m.WishlistTabComponent), canActivate: [userIdGuard] },
			{ path: "publications", loadComponent: () => import('./pages/profile/components/publications-tab/publications-tab.component').then(m => m.PublicationsTabComponent), canActivate: [idIsWriterGuard] },
		]
	},


	{ path: "change-password", loadComponent: () => import('./pages/change-password/change-password.component').then(m => m.ChangePasswordComponent), canActivate: [loggedInGuard] },
	{ path: "edit-profile", loadComponent: () => import('./pages/edit-profile/edit-profile.component').then(m => m.EditProfileComponent), canActivate: [loggedInGuard] },

	{ path: "analytics", loadComponent: () => import('./pages/analytics/analytics.component').then(m => m.AnalyticsComponent), canActivate: [loggedInGuard, writerGuard] },

	{ path: "purchases", loadComponent: () => import('./pages/purchases/purchases.component').then(m => m.PurchasesComponent), canActivate: [loggedInGuard] },
	{ path: "sales", loadComponent: () => import('./pages/sales/sales.component').then(m => m.SalesComponent), canActivate: [loggedInGuard, writerGuard] },

	{ path: "404", loadComponent: () => import('./pages/errors/not-found-page/not-found-page.component').then(m => m.NotFoundPageComponent) },
	{ path: "**", redirectTo: "404" }
];
