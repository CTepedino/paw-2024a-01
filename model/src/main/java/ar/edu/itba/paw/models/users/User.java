package ar.edu.itba.paw.models.users;

public class User {

    private final long userId;

    private final String email;
    private final String password;
    private final String firstName;
    private final String lastName;
    private final String cbu;

    public User(long userId, String email, String password, String firstName, String lastName){
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cbu = null;
    }

    public User(long userId, String email, String password, String firstName, String lastName, String cbu) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.cbu = cbu;
    }

    public long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCbu() {
        return cbu;
    }
}
