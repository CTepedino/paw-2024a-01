package ar.edu.itba.paw.webapp.dto.input;

import javax.ws.rs.Consumes;

public class OrderCreateDTO {
    private long bookId;

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }
}
