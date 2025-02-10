import {SalesCategory} from "./salesCategory";
import {BookGenre} from "./bookGenre";

export interface Book {
	id?: number;
	title?: string;
	description?: string;
	genre?: BookGenre;
	price?: number;
	pageCount?: number;
	suggestedAge?: number;
	publishDate?: string;
	isPaused?: number;
	salesCategory?: SalesCategory;
	averageRating?: number;
	orderCount?: number;
	salesTotal?: number;

	self?: string;
	writer?: string;
	cover?: string;
	preview?: string;
	bookFile?: string;
	deal?: string;
	reviews?: string;
	questions?: string;
}