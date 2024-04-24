package ar.edu.itba.paw.models;

public enum BookSearchOrderBy {
    PRICE("price"),
    PAGE_COUNT("page_count"),
    PUBLICATION_DATE("published_date");

    private final String columnName;

    BookSearchOrderBy(String columnName){
        this.columnName = columnName;
    }

    public String getColumnName(){
        return columnName;
    }
}
