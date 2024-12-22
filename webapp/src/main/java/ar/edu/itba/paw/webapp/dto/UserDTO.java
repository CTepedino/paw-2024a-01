package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.users.WriterCategory;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Collection;
import java.util.function.Function;

public class UserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String cbu;
    private String locale;
    private String description;
    private WriterCategory writerCategory;

    private URI self;
    private URI profilePicture;
    private URI roles;
    private URI changePassword;

    //private URI booksPublished;
    //private URI booksOwned;
    //etc etc

    public static Function<User, UserDTO> mapper(UriInfo uriInfo){
        return u -> fromUser(uriInfo, u);
    }


    public static UserDTO fromUser(UriInfo uriInfo, User u){
        final UserDTO dto = new UserDTO();
        dto.email = u.getEmail();
        dto.firstName = u.getFirstName();
        dto.lastName = u.getLastName();
        dto.cbu = u.getCbu();
        dto.locale = u.getLocale().toLanguageTag();
        dto.description = u.getDescription();
        dto.writerCategory = u.getWriterCategory();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).build();
        dto.profilePicture = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("profilePicture").build();
        dto.roles = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("roles").build();
        dto.changePassword = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).path("password").build();

        return dto;
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

    public URI getRoles() {
        return roles;
    }

    public void setRoles(URI roles) {
        this.roles = roles;
    }

    public URI getChangePassword() {
        return changePassword;
    }

    public void setChangePassword(URI changePassword) {
        this.changePassword = changePassword;
    }
}
