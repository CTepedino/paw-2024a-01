package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.function.Function;


public class BookDTO {

    private String title;
    private String description;

    public static Function<Book, BookDTO> mapper(UriInfo uriInfo){
        return b -> fromBook(uriInfo, b);
    }

    public static BookDTO fromBook(UriInfo uriInfo, Book b){
        final BookDTO dto = new BookDTO();
        dto.title = b.getTitle();
        dto.description = b.getDescription();

        return dto;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
