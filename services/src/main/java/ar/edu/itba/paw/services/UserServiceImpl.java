package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.dao.files.ProfilePictureDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.InvalidCodeException;
import ar.edu.itba.paw.models.exception.NoValidationCodeException;
import ar.edu.itba.paw.models.exception.UnreadableFileException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final ProfilePictureDao profilePictureDao;

    private final EmailValidationService evs;

    private final PasswordEncoder passwordEncoder;



    @Autowired
    public UserServiceImpl(final UserDao userDao, PasswordEncoder passwordEncoder, ProfilePictureDao profilePictureDao, EmailValidationService evs){
        this.userDao = userDao;
        this.profilePictureDao = profilePictureDao;
        this.passwordEncoder = passwordEncoder;
        this.evs = evs;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findById(long id){
        return userDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> findByEmail(String email) {
        LOGGER.atDebug().setMessage("Searching user by email {}").addArgument(email).log();
        return userDao.findByEmail(email);
    }

    @Transactional
    @Override
    public User create(String email, String password, String firstName, String lastName){
        User user = userDao.create(
                email,
                passwordEncoder.encode(password),
                firstName,
                lastName,
                false,
                LocaleContextHolder.getLocale()
        );

        evs.create(user);
        return user;
    }


    @Transactional
    @Override
    public void validateEmail(long id, String code) {
        Optional<User> maybeUser = userDao.findById(id);
        if (maybeUser.isPresent() && !maybeUser.get().isEnabled()){
            User user = maybeUser.get();
            if (evs.checkValidation(id, code)){
                userDao.update(user.getUserId(), user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName(), true);
                userDao.giveRole(user.getUserId(), UserRoles.READER);

                List<SimpleGrantedAuthority> authorities = getRoles(user.getUserId()).stream().map(p -> new SimpleGrantedAuthority(p.toString())).toList();
                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                throw new InvalidCodeException();
            }
        } else {
            throw new NoValidationCodeException();
        }
    }

    @Transactional
    @Override
    public void resendValidation(String email) {
        User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        evs.resend(user);
    }

    @Transactional
    @Override
    public void delete(long id) {
        userDao.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserRoles> getRoles(long id) {
        return userDao.getRoles(id);
    }

    @Transactional
    @Override
    public void giveWriterRole(long id, String cbu) {
        User user = findById(id).orElseThrow(UserNotFoundException::new);

        userDao.giveRole(id, UserRoles.WRITER);

        userDao.update(id, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName() , cbu, user.isEnabled());

        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        Collection<GrantedAuthority> updatedAuth = new HashSet<>(auth.getAuthorities());
        updatedAuth.add(new SimpleGrantedAuthority(UserRoles.WRITER.toString()));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuth);

        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getLoggedUser(){
        if (!isLoggedIn()){
            return Optional.empty();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByEmail(auth.getName());
    }

    @Override
    public boolean isLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
    }

    @Override
    public boolean hasRole(UserRoles role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!isLoggedIn()){
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(r -> r.getAuthority().equals(role.toString()));
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isCurrentUserPassword(String password) {
        User user = getLoggedUser().orElseThrow(UserNotFoundException::new);
        return passwordEncoder.matches(password, user.getPassword());
    }

    @Transactional
    @Override
    public void changePassword(String password) {
        String encodedPassword = passwordEncoder.encode(password);
        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        User user = findByEmail(auth.getName()).orElseThrow(UserNotFoundException::new);

        userDao.update(user.getUserId(), user.getEmail(),encodedPassword, user.getFirstName(), user.getLastName(), user.isEnabled());

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), encodedPassword, auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }


    @Transactional
    @Override
    public void setProfilePicture(MultipartFile profilePicture) {
        User user = getLoggedUser().orElseThrow(UserNotFoundException::new);
        try {
            profilePictureDao.createOrUpdate(user.getUserId(), profilePicture.getBytes());
        } catch (IOException e){
            throw new UnreadableFileException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<ProfilePicture> getProfilePicture(long id) {
        return profilePictureDao.findById(id);
    }
}
