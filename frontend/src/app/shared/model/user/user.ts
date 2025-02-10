import {WriterCategory} from "./writerCategory";
import {UserRoles} from "./userRoles";

export interface User {
	id?: number;
	email?: string;
	firstName?: string;
	lastName?: string;
	cbu?: string;
	locale?: string;
	description?: string;
	writerCategory?: WriterCategory;
	roles?: UserRoles[];
	orderCount?: number;
	salesTotal?: number;

	self?: string;
	profilePicture?: string;
	password?: string;
	ownedBooks?: string;
	publishedBooks?: string;
	currentMonthlyAnalytics?: string;
	wishlist?: string;
	recommendations?: string;
	askedQuestions?: string;
	receivedQuestions?: string;
	pendingQuestions?: string;
	startedOrders?: string;
	receivedOrders?: string;

}