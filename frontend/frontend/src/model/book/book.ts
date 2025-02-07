import {SalesCategory} from "./salesCategory";
import {BookGenre} from "./bookGenre";

export interface Book {
	id: number;
	title: string;
	description: string;
	genre: BookGenre;
	price: number;
	pageCount: number;
	suggestedAge: number;
	publishDate: number;
	isPaused: number;
	salesCategory: SalesCategory;
	averageRating: number;
	orderCount: number;
	salesTotal: number;

	self: string;
	writer: string;
	cover: string | null;
	preview: string | null;
	bookFile: string | null;
	deal: string | null;
	reviews: string;
	questions: string;
}