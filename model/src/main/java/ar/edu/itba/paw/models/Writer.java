package ar.edu.itba.paw.models;

public class Writer {
    private final long writerId;
    private final String name;
    private final String lastName;
    private final String email;

    public Writer(long writerId, String name, String lastName, String email) {
        this.writerId = writerId;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public long getWriterId() {
        return writerId;
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