package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.deals.Deal;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BookAndDeal {

    private final Book book;
    private final Deal deal;

    public BookAndDeal(Book book, Deal deal){
        this.book=book;
        this.deal=deal;
    }

    public Book getBook(){
        return book;
    }

    public Deal getDeal(){
        return deal;
    }

    public String getPercentage(){

        BigDecimal change = deal.getPrice().subtract(book.getPrice());
        BigDecimal percentageChange = change.divide(book.getPrice(), RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        return String.format("%+d%%", percentageChange.intValue());
    }
}
