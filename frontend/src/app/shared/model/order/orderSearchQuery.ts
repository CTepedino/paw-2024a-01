import {OrderStatus} from "./orderStatus";

export interface OrderSearchQuery {
	book_id: number | null,
	buyer_id: number | null,
	seller_id: number | null,
	title: string | null,
	status: OrderStatus | null,
	page: number | null,
	size: number | null
}