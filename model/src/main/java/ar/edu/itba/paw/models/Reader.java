package ar.edu.itba.paw.models;

public class Reader {

    private long readerId;
    private final String name;
    private final String lastName;
    private final String email;

    public Reader(long readerId, String name, String lastName, String email) {
        this.readerId = readerId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public long getReaderId() {
        return readerId;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

}
