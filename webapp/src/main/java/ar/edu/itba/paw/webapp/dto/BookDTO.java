package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSalesCategory;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.function.Function;


public class BookDTO {

    private long id;
    private String title;
    private String description;
    private BookGenre genre;
    private BigDecimal price;
    private int pageCount;
    private int suggestedAge;
    private LocalDate publishDate;
    private boolean isPaused;
    private BookSalesCategory salesCategory;
    private int averageRating;

    private URI self;
    private URI writer;
    private URI cover;
    private URI preview;
    private URI bookFile;
    private URI deal;

    public static Function<Book, BookDTO> mapper(UriInfo uriInfo){
        return b -> fromBook(uriInfo, b);
    }

    public static BookDTO fromBook(UriInfo uriInfo, Book b){
        final BookDTO dto = new BookDTO();

        dto.id = b.getBookId();
        dto.title = b.getTitle();
        dto.description = b.getDescription();
        dto.genre = b.getGenre();
        dto.price = b.getPrice();
        dto.pageCount = b.getPageCount();
        dto.suggestedAge = b.getSuggestedAge();
        dto.publishDate = b.getPublishDate();
        dto.isPaused = b.isPaused();
        dto.salesCategory = b.getSalesCategory();
        dto.averageRating = b.getAverageRating();

        dto.self = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(b.getBookId())).build();
        dto.writer = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(b.getWriter().getUserId())).build();
        dto.cover = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(b.getBookId())).path("cover").build();
        dto.preview = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(b.getBookId())).path("preview").build();
        dto.bookFile = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(b.getBookId())).path("book_file").build();
        if (b.getDeal()!=null){
            dto.deal = uriInfo.getBaseUriBuilder().path("books").path(String.valueOf(b.getBookId())).path("deal").build();
        }

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public BookGenre getGenre() {
        return genre;
    }

    public void setGenre(BookGenre genre) {
        this.genre = genre;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSuggestedAge() {
        return suggestedAge;
    }

    public void setSuggestedAge(int suggestedAge) {
        this.suggestedAge = suggestedAge;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public boolean isPaused() {
        return isPaused;
    }

    public void setPaused(boolean paused) {
        isPaused = paused;
    }

    public BookSalesCategory getSalesCategory() {
        return salesCategory;
    }

    public void setSalesCategory(BookSalesCategory salesCategory) {
        this.salesCategory = salesCategory;
    }

    public int getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(int averageRating) {
        this.averageRating = averageRating;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getWriter() {
        return writer;
    }

    public void setWriter(URI writer) {
        this.writer = writer;
    }

    public URI getCover() {
        return cover;
    }

    public void setCover(URI cover) {
        this.cover = cover;
    }

    public URI getPreview() {
        return preview;
    }

    public void setPreview(URI preview) {
        this.preview = preview;
    }

    public URI getBookFile() {
        return bookFile;
    }

    public void setBookFile(URI bookFile) {
        this.bookFile = bookFile;
    }

    public URI getDeal() {
        return deal;
    }

    public void setDeal(URI deal) {
        this.deal = deal;
    }
}
