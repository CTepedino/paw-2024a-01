package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Pattern;

@Component
public class CybraryUserDetailsService implements UserDetailsService {

    private final UserService us;

    @Autowired
    public CybraryUserDetailsService(final UserService us){
        this.us = us;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {

        final User user = us.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No user by the email" + username));

        final Collection<GrantedAuthority> authorities = new HashSet<>();
        for (UserRoles role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.toString()));
        }

        return new CybraryAuthUserDetails(user.getEmail(), user.getPassword(), authorities);
    }
}
