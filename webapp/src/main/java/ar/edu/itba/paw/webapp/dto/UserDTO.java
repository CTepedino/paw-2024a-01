package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.users.WriterCategory;

import javax.ws.rs.core.UriInfo;
import java.math.BigDecimal;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private Long orderCount;
    private BigDecimal salesTotal;

    private URI self;
    private URI profilePicture;

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
        //TODO:
        //dto.orderCount = u.getOrderCount();
        //dto.salesTotal = u.getSalesTotal();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).build();
        dto.profilePicture = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("profilePicture").build();

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

    public Long getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Long orderCount) {
        this.orderCount = orderCount;
    }

    public BigDecimal getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(BigDecimal salesTotal) {
        this.salesTotal = salesTotal;
    }
}
