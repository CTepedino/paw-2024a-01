package ar.edu.itba.paw.models;

import java.util.List;

public class PaginatedContent<T> {

    private final List<T> page;
    private final int pageNumber;
    private final int pageSize;
    private final long totalSize;

    public PaginatedContent(List<T> page, int pageNumber, int pageSize, long totalSize) {
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

    public long getTotalSize() {
        return totalSize;
    }

    public boolean hasMorePages(){
        return (long) pageNumber * pageSize < totalSize;
    }

    public int getPageCount(){
        return Math.max((int) Math.ceil((double)totalSize/pageSize), 1);
    }
}
