import {Observable} from "rxjs";
import {HttpResponse} from "@angular/common/http";

interface Pagination {
	first?: string;
	last?: string;
	previous?: string;
	next?: string;
	totalCount: number;
	pageCount: number;
}

export interface PaginatedContent<T> {
	data: T[];
	pagination: Pagination
}

export function setPagination<T>(response: HttpResponse<T[]>, size: number): PaginatedContent<T> {
	const linkHeader = response.headers.get('link');
	const totalCount = response.headers.get('x-total-count');
	const links: Record<string, string> = {};

	if (linkHeader) {
		const regex = /<([^>]+)>;\s*rel="([^"]+)"/g;
		let match;

		while ((match = regex.exec(linkHeader)) !== null) {
			links[match[2]] = match[1];
		}

	}

	return {
		data: Array.isArray(response.body) ? response.body : [],
		pagination: {
			...links,
			totalCount: Number(totalCount) ?? 0,
			pageCount: Math.ceil(Number(totalCount)/size) ?? 1
		}
	};

}