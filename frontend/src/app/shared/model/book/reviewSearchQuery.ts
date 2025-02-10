import {ReviewOrderBy} from "./reviewOrderBy";

export interface ReviewSearchQuery {
	order_by?: ReviewOrderBy,
	page?: number,
	size?: number
}