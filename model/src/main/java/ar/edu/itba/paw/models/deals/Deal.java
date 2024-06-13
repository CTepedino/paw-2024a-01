package ar.edu.itba.paw.models.deals;

import ar.edu.itba.paw.models.books.Book;

import javax.persistence.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Locale;

@Entity
@Table(name="deals")
public class Deal {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "deals_deal_id_seq")
    @SequenceGenerator(sequenceName = "deals_deal_id_seq", name = "deals_deal_id_seq", allocationSize = 1)
    @Column(name = "deal_id")
    private Long dealId;

    @Column(name = "book_id", nullable = false)
    private long bookId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    /* default */ Deal(){}

    public Deal(long bookId, BigDecimal price, LocalDate startDate, LocalDate endDate){
        this.bookId=bookId;
        this.price=price;
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public String getFormattedPrice(){
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale.Builder().setLanguage("es").setRegion("AR").build());
        currencyFormatter.setMaximumFractionDigits(0);
        return currencyFormatter.format(price);
    }

    public long getDealId() {
        return dealId;
    }

    public long getBookId() {
        return bookId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate){
        this.endDate=endDate;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
