package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CybraryUserDetailsService implements UserDetailsService {

    private final UserService us;
    private final EmailValidationService evs;

    @Autowired
    public CybraryUserDetailsService(final UserService us, final EmailValidationService evs){
        this.us = us;
        this.evs = evs;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        evs.deleteExpired();
        final User user = us.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user by the email" + username));
        String password = user.getPassword();

        if (user.getPassword() == null){
            password = us.fillMissingWriterData(user.getUserId(),user.getEmail());
        }

        if (!user.isEnabled()){
            us.resendValidation(username);
        }

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.toString())).toList();

        return new CybraryAuthUserDetails(user.getEmail(), password, user.isEnabled(), true, true, true, authorities);
    }


}
