export interface Answer {
	id?: number;
	bookId?: number;
	questionerId?: number;
	writerId?: number;
	answer?: string;
	answerDate?: string;

	self?: string;
	question?: string;
	questioner?: string;
	writer?: string;
	book?: string;
}