package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class EmailValidationAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String code;

    public EmailValidationAuthenticationToken(String email, String code) {
        super(null);
        this.email = email;
        this.code = code;
        this.setAuthenticated(true);
    }


    @Override
    public Object getCredentials() {
        return email;
    }

    @Override
    public Object getPrincipal() {
        return code;
    }
}
