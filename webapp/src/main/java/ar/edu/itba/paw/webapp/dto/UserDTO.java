package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.users.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.function.Function;

public class UserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String password; //write-only

    private URI self;
    private URI reportedIssues;
    private URI assignedIssues;

    public static Function<User, UserDTO> mapper(UriInfo uriInfo){
        return u -> fromUser(uriInfo, u);
    }


    public static UserDTO fromUser(UriInfo uriInfo, User u){
        final UserDTO dto = new UserDTO();
        dto.email = u.getEmail();
        dto.firstName = u.getFirstName();
        dto.lastName = u.getLastName();

        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(u.getUserId())).build();
        //dto.reported y dto.assigned...


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


    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getReportedIssues() {
        return reportedIssues;
    }

    public void setReportedIssues(URI reportedIssues) {
        this.reportedIssues = reportedIssues;
    }

    public URI getAssignedIssues() {
        return assignedIssues;
    }

    public void setAssignedIssues(URI assignedIssues) {
        this.assignedIssues = assignedIssues;
    }
}
