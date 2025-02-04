package ar.edu.itba.paw.models.deals;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name="deals")
public class Deal {

    @Id
    @Column(name = "id")
    private long dealId;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    Deal(){}

    public Deal(long dealId, BigDecimal price, LocalDate startDate, LocalDate endDate){
        this.dealId=dealId;
        this.price=price;
        this.startDate=startDate;
        this.endDate=endDate;
    }

    public long getDealId() {
        return dealId;
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
