export interface WriterMonthlyAnalytics {
	writerId?: number;
	orderCount?: number;
	salesTotal?: number;
	month?: string;

	self?: string;
	writer?: string;
	nextMonth?: string;
	prevMonth?: string;
}