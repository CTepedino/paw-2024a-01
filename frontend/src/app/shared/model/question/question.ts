export interface Question {
	id?: number;
	bookId?: number;
	questionerId?: number;
	writerId?: number;
	question?: string;
	date?: string;

	self?: string;
	book?: string;
	questioner?: string;
	writer?: string;
	answer?: string;
}