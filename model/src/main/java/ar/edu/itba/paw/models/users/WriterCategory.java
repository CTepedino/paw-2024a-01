package ar.edu.itba.paw.models.users;

public enum WriterCategory {
    DEFAULT(0L),
    BRONZE(5L),
    SILVER(10L),
    GOLD(20L);

    private final Long minSales;

    WriterCategory(Long minSales){
        this.minSales=minSales;
    }

    public Long getMinSales(){return minSales;}

}