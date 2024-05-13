package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.orders.OrderStatus;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class OrderSearchForm {
    private String title;

    private OrderStatus orderStatus;

    @NotNull
    @Min(1)
    private Integer page = 1;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

}
