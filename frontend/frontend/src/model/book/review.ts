export interface Review {
	reviewerId: number;
	rating: number;
	review: string;
	date: string;

	self: string;
	book: string;
	reviewer: string;
}