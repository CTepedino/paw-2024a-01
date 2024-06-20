package ar.edu.itba.paw.models.books;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class AnalyticsBook {

    private Book book;
    private Long totalOrders;

    private BigDecimal totalSales;

    public AnalyticsBook(Book book, Long totalOrders, BigDecimal totalSales){
        this.book=book;
        this.totalOrders=totalOrders;
        this.totalSales=totalSales;
        if(this.totalSales == null){
            this.totalSales= BigDecimal.valueOf(0);
        }
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public BigDecimal getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(BigDecimal totalSales) {
        this.totalSales = totalSales;
    }

    public String getFormattedTotalSales(){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("AR").build());
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(totalSales);
    }
}
