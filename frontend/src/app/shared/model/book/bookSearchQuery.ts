import {BookGenre} from "./bookGenre";
import {BookSearchOrderBy} from "./bookSearchOrderBy";

export interface BookSearchQuery {
	page?: number,
	size?: number,
	title?: string,
	genre?: BookGenre,
	min_price?: number,
	max_price?: number,
	min_page_count?: number,
	max_page_count?: number,
	min_suggested_age?: number,
	max_suggested_age?: number,
	order_by?: BookSearchOrderBy,
	writer_id?: number,
	owner_id?: number,
	recommendations_for_book?: number
}
