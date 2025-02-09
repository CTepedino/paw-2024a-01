export interface QuestionSearchQuery {
	book_id: number | null,
	writer_id: number | null,
	questioner_id: number | null,
	exclude_questioner: boolean | null,
	is_answered: boolean | null,
	page: number | null,
	size: number | null
}