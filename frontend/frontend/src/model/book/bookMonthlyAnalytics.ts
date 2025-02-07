export interface BookMonthlyAnalytics {
	bookId: number;
	orderCount: number;
	salesTotal: number;
	month: string;

	self: string;
	book: string;
	nextMonth: string | null;
	prevMonth: string;
}