import {Review} from "./review";
import {User} from "../user/user";

export interface ReviewWithInfo extends Review{
	reviewerInfo?: Partial<User>
}