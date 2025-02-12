package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.users.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Optional;

public class IndexDTO {

    private URI self;
    private URI users;
    private URI genres;
    private URI books;
    private URI orders;
    private URI questions;
    private URI resetCodes;
    private URI validationCodes;
    private URI loggedUser;

    public static IndexDTO create(UriInfo uriInfo, Optional<User> user){
        IndexDTO dto = new IndexDTO();

        dto.self = uriInfo.getBaseUriBuilder().build();
        dto.users = uriInfo.getBaseUriBuilder().path("users").build();
        dto.genres = uriInfo.getBaseUriBuilder().path("genres").build();
        dto.books = uriInfo.getBaseUriBuilder().path("books").build();
        dto.orders = uriInfo.getBaseUriBuilder().path("orders").build();
        dto.questions = uriInfo.getBaseUriBuilder().path("questions").build();
        dto.resetCodes = uriInfo.getBaseUriBuilder().path("reset-password-codes").build();
        dto.validationCodes = uriInfo.getBaseUriBuilder().path("validation-codes").build();
        user.ifPresent(u -> dto.loggedUser = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).build());

        return dto;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getUsers() {
        return users;
    }

    public void setUsers(URI users) {
        this.users = users;
    }

    public URI getGenres() {
        return genres;
    }

    public void setGenres(URI genres) {
        this.genres = genres;
    }

    public URI getBooks() {
        return books;
    }

    public void setBooks(URI books) {
        this.books = books;
    }

    public URI getOrders() {
        return orders;
    }

    public void setOrders(URI orders) {
        this.orders = orders;
    }

    public URI getQuestions() {
        return questions;
    }

    public void setQuestions(URI questions) {
        this.questions = questions;
    }

    public URI getResetCodes() {
        return resetCodes;
    }

    public void setResetCodes(URI resetCodes) {
        this.resetCodes = resetCodes;
    }

    public URI getValidationCodes() {
        return validationCodes;
    }

    public void setValidationCodes(URI validationCodes) {
        this.validationCodes = validationCodes;
    }

    public URI getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(URI loggedUser) {
        this.loggedUser = loggedUser;
    }
}
