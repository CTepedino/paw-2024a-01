package ar.edu.itba.paw.models.users;

import java.time.LocalDateTime;

public class EmailValidation {
    private final long id;
    private final String code;
    private final LocalDateTime expiration;

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpiration(){
        return expiration;
    }

    public EmailValidation(long id, String code, LocalDateTime expiration) {
        this.id = id;
        this.code = code;
        this.expiration = expiration;
    }
}


