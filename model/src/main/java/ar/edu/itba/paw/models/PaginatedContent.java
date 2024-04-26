package ar.edu.itba.paw.models;

import java.util.List;

public class PaginatedContent<T> {

    private final List<T> page;
    private final int pageNumber;
    private final int pageSize;
    private final int totalSize;

    public PaginatedContent(List<T> page, int pageNumber, int pageSize, int totalSize) {
        this.page = page;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalSize = totalSize;
    }

    public List<T> getPage() {
        return page;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public boolean isLastPage(){
        return pageNumber * pageSize == totalSize;
    }

    public int getPageCount(){
        return Math.max((int) Math.ceil((double)totalSize/pageSize), 1);
    }
}

//LIMIT -> #rows returned
//OFFSET -> #rows skipped before starting to return