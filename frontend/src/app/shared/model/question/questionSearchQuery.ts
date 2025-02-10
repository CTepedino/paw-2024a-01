export interface QuestionSearchQuery {
	book_id?: number,
	writer_id?: number,
	questioner_id?: number,
	exclude_questioner?: boolean,
	is_answered?: boolean,
	page?: number,
	size?: number
}