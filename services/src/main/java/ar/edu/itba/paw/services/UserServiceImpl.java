package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.EmailValidationDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.dao.files.ProfilePictureDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.InvalidCodeException;
import ar.edu.itba.paw.models.exception.NoValidationCodeException;
import ar.edu.itba.paw.models.exception.UnreadableFileException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.EmailValidation;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.time.LocalDateTime;
import java.util.*;

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
                lastName
        );
        userDao.giveRole(user.getUserId(), UserRoles.UNVALIDATED);
        evs.create(user.getUserId());

        return user;
    }


    @Transactional
    @Override
    public void validateEmail(String email, String code) {
        Optional<User> user = userDao.findByEmail(email);
        if (user.isPresent() && getRoles(user.get().getUserId()).contains(UserRoles.UNVALIDATED)){
            long id = user.get().getUserId();
            if (evs.checkValidation(id, email, code)){
                userDao.giveRole(id, UserRoles.READER);
                userDao.removeRole(id, UserRoles.UNVALIDATED);
            } else {
                throw new InvalidCodeException();
            }
        } else {
            throw new NoValidationCodeException();
        }

    }

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

        userDao.update(id, user.getEmail(), user.getPassword(), user.getFirstName(), user.getLastName() , cbu);

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

    @Override
    public boolean isLoggedIn() {
        return SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
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

        userDao.update(user.getUserId(), user.getEmail(),encodedPassword, user.getFirstName(), user.getLastName());

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
