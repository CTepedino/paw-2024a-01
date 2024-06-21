package ar.edu.itba.paw.models.books;

public enum BookSalesCategory {
    BEST_SELLER(3L),
    POPULAR(1L),

    DEFAULT(0L);

    private final Long minSales;

    BookSalesCategory(Long minSales){
        this.minSales=minSales;
    }

    public Long getMinSales(){
        return minSales;
    }

}
