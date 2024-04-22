package ar.edu.itba.paw.models;

public class User {

    private final long userId;
    private final UserRoles[] roles;

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;

    public User(long userId, UserRoles[] roles, String firstName, String lastName, String email, String password) {
        this.userId = userId;
        this.roles = roles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public long getUserId() {
        return userId;
    }

    public UserRoles[] getRoles() {
        return roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
