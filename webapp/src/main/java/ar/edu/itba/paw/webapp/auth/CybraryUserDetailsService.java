package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.UserService;
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

        //Para los escritores que fueron creados antes de tener usuarios -> les ponemos como password por defecto su mail
        if (user.getPassword() == null){
            us.fillMissingWriterData(user.getUserId(), user.getEmail());
            final Collection<GrantedAuthority> authorities = new HashSet<>();
            authorities.add(new SimpleGrantedAuthority(UserRoles.READER.toString()));
            authorities.add(new SimpleGrantedAuthority(UserRoles.WRITER.toString()));
            return new CybraryAuthUserDetails(user.getEmail(), user.getEmail(), authorities);
        } else {

            final Collection<GrantedAuthority> authorities = new HashSet<>();
            for (UserRoles role : user.getRoles()) {
                authorities.add(new SimpleGrantedAuthority(role.toString()));
            }

            return new CybraryAuthUserDetails(user.getEmail(), user.getPassword(), authorities);
        }
    }
}
