package ar.edu.itba.paw.webapp.dto.output;


import ar.edu.itba.paw.models.books.WishlistItem;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.function.Function;

public class WishlistDTO {
    private long bookId;

    private URI self;
    private URI book;
    private URI user;

    public static Function<WishlistItem, WishlistDTO> mapper(UriInfo uriInfo){
        return w -> fromWishlistItem(uriInfo, w);
    }

    public static WishlistDTO fromWishlistItem(UriInfo uriInfo, WishlistItem w){
        final WishlistDTO dto = new WishlistDTO();
        dto.bookId = w.getBookId();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(w.getUserId())).path("wishlist").path(String.valueOf(w.getBookId())).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(w.getBookId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(w.getUserId())).build();

        return dto;
    }

    public long getBookId() {
        return bookId;
    }

    public void setBookId(long bookId) {
        this.bookId = bookId;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getBook() {
        return book;
    }

    public void setBook(URI book) {
        this.book = book;
    }

    public URI getUser() {
        return user;
    }

    public void setUser(URI user) {
        this.user = user;
    }
}
