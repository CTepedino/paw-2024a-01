import {BookGenre} from "./bookGenre";

export interface SearchQuery {
	page: number | null,
	size: number | null,
	title: string | null,
	genre: BookGenre | null,
	minPrice: number | null,
	maxPrice: number | null,
	minPageCount: number | null,
	maxPageCount: number | null,
	minSuggestedAge: number | null,
	orderBy: number | null,
	writerId: number | null,
	ownerId: number | null,
	recommendationsForBook: number | null
}
