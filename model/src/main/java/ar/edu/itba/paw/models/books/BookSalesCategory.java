package ar.edu.itba.paw.models.books;

public enum BookSalesCategory {
    BEST_SELLER(3),
    POPULAR(1),

    DEFAULT(0);

    private final Integer minSales;

    BookSalesCategory(Integer minSales){
        this.minSales=minSales;
    }

    public Integer getMinSales(){
        return minSales;
    }

}
