import {Order} from "./order";
import {Book} from "../book/book";
import {User} from "../user/user";

export interface OrderWithData extends Order {
	bookInfo: Partial<Book>,
	writerInfo?: Partial<User>,
	buyerInfo?: Partial<User>
}