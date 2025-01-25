package ar.edu.itba.paw.models.books;

public enum BookSearchOrderBy {
    PRICE_ASC("COALESCE(d.price, b.price) asc", "price.asc"),
    PRICE_DESC("COALESCE(d.price, b.price) desc", "price.desc"),
    PAGE_COUNT_ASC("page_count asc", "pageCount.asc"),
    PAGE_COUNT_DESC("page_count desc", "pageCount.desc"),
    PUBLICATION_DATE_ASC("published_date asc", "publicationDate.asc"),
    PUBLICATION_DATE_DESC("published_date desc", "publicationDate.desc"),
    BEST_SELLERS("COUNT(o.book_id) desc", null),
    NEW_DEALS("d.start_date desc", null);

    private final String columnName;
    private final String messageCode;

    BookSearchOrderBy(String columnName, String messageCode){
        this.columnName = columnName;
        this.messageCode = messageCode;
    }

    public String getColumnName(){
        return columnName;
    }

    public String getMessageCode(){
        return messageCode;
    }
}
