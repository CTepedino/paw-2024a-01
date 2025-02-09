import {BookGenre} from "./bookGenre";
import {BookSearchOrderBy} from "./bookSearchOrderBy";

export interface BookSearchQuery {
	page: number | null,
	size: number | null,
	title: string | null,
	genre: BookGenre | null,
	min_price: number | null,
	max_price: number | null,
	min_page_count: number | null,
	max_page_count: number | null,
	min_suggested_age: number | null,
	order_by: BookSearchOrderBy | null,
	writer_id: number | null,
	owner_id: number | null,
	recommendations_for_book: number | null
}
