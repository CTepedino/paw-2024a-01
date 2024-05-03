package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(long id){
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        return userDao.findByEmail(email);
    }

    @Transactional
    @Override
    public User create(String firstName, String lastName, String email, String password){
        User user =  userDao.create(
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password)
        );
        userDao.giveRole(user.getUserId(), UserRoles.READER);
        return user;
    }

    @Override
    public List<UserRoles> getRoles(long id) {
        return userDao.getRoles(id);
    }

    /*
    @Transactional
    @Override
    public void fillMissingWriterData(long id, String password) {
        if (userDao.findById(id).isPresent()) {
            userDao.changePassword(id, passwordEncoder.encode(password));
            userDao.giveRole(id, UserRoles.READER);
            userDao.giveRole(id, UserRoles.WRITER);
        }
    }*/

    @Transactional
    @Override
    public void giveWriterRole(long id) {
        userDao.giveRole(id, UserRoles.WRITER);

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
        User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
        userDao.update(user.getUserId(), user.getEmail(), passwordEncoder.encode(password), user.getFirstName(), user.getLastName());

    }

}
