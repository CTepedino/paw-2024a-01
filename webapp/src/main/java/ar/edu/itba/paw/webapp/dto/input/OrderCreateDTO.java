package ar.edu.itba.paw.webapp.dto.input;

import javax.ws.rs.Consumes;

public class OrderCreateDTO {
    private long bookId;
    private long userId; //TODO: from jwt

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
