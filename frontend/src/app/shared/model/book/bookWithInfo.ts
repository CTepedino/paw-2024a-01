import {Book} from "./book";
import {User} from "../user/user";
import {Deal} from "./deal";

export interface BookWithInfo {
	book: Partial<Book>
	writer: Partial<User>
	deal?: Partial<Deal>
}