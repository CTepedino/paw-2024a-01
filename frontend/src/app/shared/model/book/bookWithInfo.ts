import {Book} from "./book";
import {User} from "../user/user";
import {Deal} from "./deal";

export interface BookWithInfo extends Book{
	writerInfo: Partial<User>
	dealInfo?: Partial<Deal>
}