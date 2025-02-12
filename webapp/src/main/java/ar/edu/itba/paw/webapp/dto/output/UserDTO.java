package ar.edu.itba.paw.webapp.dto.output;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.users.WriterCategory;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.YearMonth;
import java.util.Collection;
import java.util.function.Function;

public class UserDTO {

    private long id;
    private String email;
    private String firstName;
    private String lastName;
    private String cbu;
    private String locale;
    private String description;
    private WriterCategory writerCategory;
    private Collection<UserRoles> roles;
    private long orderCount;
    private double salesTotal;

    private URI self;
    private URI profilePicture;
    private URI password;
    private URI ownedBooks;
    private URI publishedBooks;
    private URI currentMonthlyAnalytics;
    private URI wishlist;
    private URI recommendations;
    private URI askedQuestions;
    private URI receivedQuestions;
    private URI pendingQuestions;
    private URI startedOrders;
    private URI receivedOrders;

    public static Function<User, UserDTO> mapper(UriInfo uriInfo){
        return u -> fromUser(uriInfo, u);
    }

    public static UserDTO fromUser(UriInfo uriInfo, User u){
        final UserDTO dto = new UserDTO();
        dto.id = u.getUserId();
        dto.email = u.getEmail();
        dto.firstName = u.getFirstName();
        dto.lastName = u.getLastName();
        dto.cbu = u.getCbu();
        dto.locale = u.getLocale().toLanguageTag();
        dto.description = u.getDescription();
        dto.writerCategory = u.getWriterCategory();
        dto.roles = u.getRoles();

        dto.orderCount = u.getOrderCount();
        dto.salesTotal = u.getSalesTotal();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).build();
        dto.profilePicture = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("profile-picture").build();
        dto.password = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("password").build();
        dto.ownedBooks = uriInfo.getBaseUriBuilder().path("books").queryParam("owner_id", u.getUserId()).build();
        dto.wishlist = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("wishlist").build();
        dto.recommendations = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("recommendations").build();
        dto.askedQuestions = uriInfo.getBaseUriBuilder().path("questions").queryParam("questioner_id", u.getUserId()).build();
        dto.startedOrders = uriInfo.getBaseUriBuilder().path("orders").queryParam("buyer_id", u.getUserId()).build();
        if (u.getRoles().contains(UserRoles.WRITER)){
            dto.publishedBooks = uriInfo.getBaseUriBuilder().path("books").queryParam("writer_id", u.getUserId()).build();
            dto.currentMonthlyAnalytics = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("monthly-analytics").path(YearMonth.now().toString()).build();
            dto.receivedQuestions = uriInfo.getBaseUriBuilder().path("questions").queryParam("writer_id", u.getUserId()).build();
            dto.pendingQuestions = uriInfo.getBaseUriBuilder().path("questions").queryParam("writer_id", u.getUserId()).queryParam("is_answered", false).build();
            dto.receivedOrders = uriInfo.getBaseUriBuilder().path("orders").queryParam("seller_id", u.getUserId()).build();
        }

        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public WriterCategory getWriterCategory() {
        return writerCategory;
    }

    public void setWriterCategory(WriterCategory writerCategory) {
        this.writerCategory = writerCategory;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public URI getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(URI profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Collection<UserRoles> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRoles> roles) {
        this.roles = roles;
    }

    public long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(long orderCount) {
        this.orderCount = orderCount;
    }

    public double getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(double salesTotal) {
        this.salesTotal = salesTotal;
    }

    public URI getPassword() {
        return password;
    }

    public void setPassword(URI password) {
        this.password = password;
    }

    public URI getOwnedBooks() {
        return ownedBooks;
    }

    public void setOwnedBooks(URI ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public URI getPublishedBooks() {
        return publishedBooks;
    }

    public void setPublishedBooks(URI publishedBooks) {
        this.publishedBooks = publishedBooks;
    }

    public URI getCurrentMonthlyAnalytics() {
        return currentMonthlyAnalytics;
    }

    public void setCurrentMonthlyAnalytics(URI currentMonthlyAnalytics) {
        this.currentMonthlyAnalytics = currentMonthlyAnalytics;
    }

    public URI getWishlist() {
        return wishlist;
    }

    public void setWishlist(URI wishlist) {
        this.wishlist = wishlist;
    }

    public URI getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(URI recommendations) {
        this.recommendations = recommendations;
    }

    public URI getAskedQuestions() {
        return askedQuestions;
    }

    public void setAskedQuestions(URI askedQuestions) {
        this.askedQuestions = askedQuestions;
    }

    public URI getReceivedQuestions() {
        return receivedQuestions;
    }

    public void setReceivedQuestions(URI receivedQuestions) {
        this.receivedQuestions = receivedQuestions;
    }

    public URI getPendingQuestions() {
        return pendingQuestions;
    }

    public void setPendingQuestions(URI pendingQuestions) {
        this.pendingQuestions = pendingQuestions;
    }

    public URI getStartedOrders() {
        return startedOrders;
    }

    public void setStartedOrders(URI startedOrders) {
        this.startedOrders = startedOrders;
    }

    public URI getReceivedOrders() {
        return receivedOrders;
    }

    public void setReceivedOrders(URI receivedOrders) {
        this.receivedOrders = receivedOrders;
    }
}
