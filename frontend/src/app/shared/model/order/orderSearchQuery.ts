import {OrderStatus} from "./orderStatus";

export interface OrderSearchQuery {
	book_id?: number,
	buyer_id?: number,
	seller_id?: number,
	title?: string,
	status?: OrderStatus,
	page?: number,
	size?: number
}