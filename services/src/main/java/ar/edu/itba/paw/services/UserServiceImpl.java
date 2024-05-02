package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRoles;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao, PasswordEncoder passwordEncoder){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findById(long id){
        return userDao.findById(id);
    }

    @Transactional
    @Override
    public User create(String firstName, String lastName, String email, String password){
        return userDao.create(
                new UserRoles[]{UserRoles.READER},
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password)
        );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    @Override
    public void fillMissingWriterData(long id, String password) {
        if (userDao.findById(id).isPresent()) {
            userDao.changePassword(id, passwordEncoder.encode(password));
            userDao.giveRole(id, UserRoles.READER);
            userDao.giveRole(id, UserRoles.WRITER);
        }
    }
    @Transactional
    @Override
    public void giveWriterRole(long id) {
        User user = userDao.giveRole(id, UserRoles.WRITER);
        //userDao.setNames(id, firstName, lastName);

        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        Collection<GrantedAuthority> updatedAuth = new HashSet<>(auth.getAuthorities());
        updatedAuth.add(new SimpleGrantedAuthority(UserRoles.WRITER.toString()));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuth);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()){
            return Optional.empty();
        }
        return findByEmail(auth.getName());
    }

    @Transactional
    @Override
    public void changePassword(long id, String password) {
        userDao.changePassword(id, passwordEncoder.encode(password));
    }

}
