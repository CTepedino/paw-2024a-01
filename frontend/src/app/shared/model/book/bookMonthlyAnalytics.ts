export interface BookMonthlyAnalytics {
	bookId?: number;
	orderCount?: number;
	salesTotal?: number;
	month?: string;

	self?: string;
	book?: string;
	nextMonth?: string;
	prevMonth?: string;
}