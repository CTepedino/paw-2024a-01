package ar.edu.itba.paw.webapp.auth;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class ResetPasswordCodeAuthenticationToken extends AbstractAuthenticationToken {

    private final String email;
    private final String code;

    public ResetPasswordCodeAuthenticationToken(String email, String code){
        super(null);
        this.email = email;
        this.code = code;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return code;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }
}
