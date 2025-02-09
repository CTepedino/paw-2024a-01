import {OrderStatus} from "./orderStatus";

export interface Order {
	orderId: number;
	buyerId: number;
	bookId: number;
	sellerId: number;
	status: OrderStatus;
	date: string;
	rejectedReason: string | null;
	price: string;

	self: string;
	book: string;
	buyer: string;
	seller: string;
	receipt: string | null;
}