package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.deals.Deal;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;

public class DealDTO {

    private long id;
    private BigDecimal price;
    private LocalDate start;
    private LocalDate end;

    private URI self;
    private URI book;

    public static DealDTO fromDeal(UriInfo uriInfo, Deal d){
        DealDTO dto = new DealDTO();

        dto.id = d.getDealId();
        dto.price = d.getPrice();
        dto.start = d.getStartDate();
        dto.end = d.getEndDate();

        dto.self = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.id)).path("deal").build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(dto.id)).build();

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public void setEnd(LocalDate end) {
        this.end = end;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
