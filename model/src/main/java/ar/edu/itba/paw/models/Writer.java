package ar.edu.itba.paw.models;

public class Writer {
    private final long writer_id;
    private final String name;
    private final String lastName;
    private final String email;

    public Writer(long writer_id, String name, String lastName, String email) {
        this.writer_id = writer_id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }

    public long getWriterId() {
        return writer_id;
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