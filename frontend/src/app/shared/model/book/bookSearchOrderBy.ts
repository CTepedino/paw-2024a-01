export enum BookSearchOrderBy {
	PRICE_ASC = "PRICE_ASC",
	PRICE_DESC = "PRICE_DESC",
	PAGE_COUNT_ASC = "PAGE_COUNT_ASC",
	PAGE_COUNT_DESC = "PAGE_COUNT_DESC",
	PUBLICATION_DATE_ASC = "PUBLICATION_DATE_ASC",
	PUBLICATION_DATE_DESC = "PUBLICATION_DATE_DESC",
	BEST_SELLERS = "BEST_SELLERS",
	NEW_DEALS = "NEW_DEALS"
}

export const BookSearchOrderByOptions = [
	{ label: "Publication date: Most recent", value: BookSearchOrderBy.PUBLICATION_DATE_DESC },
	{ label: "Publication date: Oldest", value: BookSearchOrderBy.PUBLICATION_DATE_ASC },
	{ label: "Page count: Descending", value: BookSearchOrderBy.PAGE_COUNT_DESC },
	{ label: "Page count: Ascending", value: BookSearchOrderBy.PAGE_COUNT_ASC },
	{ label: "Price: Descending", value: BookSearchOrderBy.PRICE_DESC },
	{ label: "Price: Ascending", value: BookSearchOrderBy.PRICE_ASC },
]