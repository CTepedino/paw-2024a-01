package ar.edu.itba.paw.models.books;

public enum BookSearchOrderBy {
    PRICE_ASC("COALESCE(d.price, b.price) asc", "COALESCE(d.price, b.price) asc", "price.asc"),
    PRICE_DESC("COALESCE(d.price, b.price) desc", "COALESCE(d.price, b.price) desc", "price.desc"),
    PAGE_COUNT_ASC("page_count asc",  "page_count asc","pageCount.asc"),
    PAGE_COUNT_DESC("page_count desc",  "page_count desc","pageCount.desc"),
    PUBLICATION_DATE_ASC("b.published_date asc", "b.publishDate  asc" ,"publicationDate.asc"),
    PUBLICATION_DATE_DESC("b.published_date desc ", "b.publishDate desc" ,"publicationDate.desc"),
    BEST_SELLERS("COUNT(o.book_id) desc", "b.bookId desc" ,null),
    NEW_DEALS("d.start_date desc", "d.start_date desc" ,null),
    LATEST_BOOKS("b.book_id desc", "b.bookId desc", null);


    private final String columnName;
    private final String modelName;
    private final String messageCode;

    BookSearchOrderBy(String columnName, String modelName, String messageCode){
        this.columnName = columnName;
        this.modelName = modelName;
        this.messageCode = messageCode;
    }

    public String getColumnName(){
        return columnName;
    }

    public String getModelName(){
        return modelName;
    }

    public String getMessageCode(){
        return messageCode;
    }
}
