package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.books.Recommendation;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.function.Function;

public class RecommendationDTO {
    private Long bookId;

    private URI self;
    private URI book;
    private URI user;

    public static Function<Recommendation, RecommendationDTO> mapper(UriInfo uriInfo){
        return w -> fromRecommendation(uriInfo, w);
    }

    public static RecommendationDTO fromRecommendation(UriInfo uriInfo, Recommendation r){
        final RecommendationDTO dto = new RecommendationDTO();
        dto.bookId = r.getBookId();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(r.getUserId())).path("wishlist").path(String.valueOf(r.getBookId())).build();
        dto.book = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(r.getBookId())).build();
        dto.user = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(r.getUserId())).build();

        return dto;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
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
