package ar.edu.itba.paw.models.users;

import java.time.LocalDateTime;

public class EmailValidation {
    private final String email;
    private final String code;
    private final LocalDateTime expiration;

    public String getEmail() {
        return email;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpiration(){
        return expiration;
    }

    public EmailValidation(String email, String code, LocalDateTime expiration) {
        this.email = email;
        this.code = code;
        this.expiration = expiration;
    }
}


