import {ReviewOrderBy} from "./reviewOrderBy";

export interface ReviewSearchQuery {
	order_by: ReviewOrderBy | null,
	page: number | null,
	size: number | null
}