package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.ImageNotFoundException;
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
import org.springframework.scheduling.annotation.Scheduled;
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
import java.io.InputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    private final EmailValidationService evs;

    private final PasswordEncoder passwordEncoder;

    private final MailService ms;

    @Autowired
    public UserServiceImpl(final UserDao userDao, PasswordEncoder passwordEncoder, EmailValidationService evs, MailService ms){
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
        this.evs = evs;
        this.ms = ms;
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
        LOGGER.atDebug().setMessage("Created user: {}").addArgument(firstName).log();
        return user;
    }


    @Transactional
    @Override
    public void validateEmail(long id, String code) {
        Optional<User> maybeUser = userDao.findById(id);
        if (maybeUser.isPresent() && !maybeUser.get().isEnabled()){
            User user = maybeUser.get();
            if (evs.checkValidation(id, code)){

                userDao.updateIsEnabled(user,true);
                userDao.giveRole(user, UserRoles.READER);

                List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.toString())).toList();
                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                LOGGER.atWarn().setMessage("Failed to Validate email for userId: {} - Invalid Code").addArgument(id).log();
                throw new InvalidCodeException();
            }
        } else {
            LOGGER.atWarn().setMessage("Failed to Validated email for userId: {} - No validation code").addArgument(id).log();
            throw new NoValidationCodeException();
        }
        LOGGER.atDebug().setMessage("Validated email for userId: {}").addArgument(id).log();
    }

    @Transactional
    @Override
    public void resendValidation(String email) {
        User user = userDao.findByEmail(email).orElseThrow(UserNotFoundException::new);
        evs.resend(user);
    }

    @Transactional
    @Override
    public void giveWriterRole(String cbu) {
        User user = getLoggedUser().orElseThrow(UserNotFoundException::new);

        userDao.giveRole(user, UserRoles.WRITER);

        userDao.update(user, user.getFirstName(), user.getLastName(), cbu, user.getDescription());

        Authentication auth =  SecurityContextHolder.getContext().getAuthentication();

        Collection<GrantedAuthority> updatedAuth = new HashSet<>(auth.getAuthorities());
        updatedAuth.add(new SimpleGrantedAuthority(UserRoles.WRITER.toString()));

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), updatedAuth);

        SecurityContextHolder.getContext().setAuthentication(newAuth);

        LOGGER.atDebug().setMessage("Gave writer role to userId: {}").addArgument(user.getUserId()).log();
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


        userDao.updatePassword(user, encodedPassword);

        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), encodedPassword, auth.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        LOGGER.atDebug().setMessage("Changed password for userId: {}").addArgument(user.getUserId()).log();
    }

    @Transactional
    @Override
    public void updateProfile(String firstName, String lastName, String cbu, MultipartFile profilePicture, String description) {
        User user = getLoggedUser().orElseThrow(UserNotFoundException::new);

        String oldCbu = user.getCbu();

        userDao.update(user, firstName, lastName, cbu, description);

        if ((user.getRoles().contains(UserRoles.WRITER) && oldCbu==null ) || user.getRoles().isEmpty()){
            userDao.recheckAllPaused(user.getUserId());
        }


        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                if (user.getProfilePicture()==null){
                    userDao.createProfilePicture(user, profilePicture.getBytes());
                } else {
                    userDao.updateProfilePicture(user, profilePicture.getBytes());
                }
            } catch (IOException e){
                LOGGER.atWarn().setMessage("Failed to update profile for user: {} - Error Message: {}").addArgument(firstName).addArgument(e.getMessage()).log();
                throw new UnreadableFileException();
            }
        }
        LOGGER.atDebug().setMessage("Updated profile for user: {}").addArgument(firstName).log();
    }

    @Transactional(readOnly = true)
    @Override
    public ProfilePicture getProfilePictureOrDefault(long id) {
        User user = userDao.findById(id).orElseThrow(UserNotFoundException::new);
        if (user.getProfilePicture()!=null) {
            return user.getProfilePicture();
        }
        return new ProfilePicture(user.getUserId(), getDefaultProfilePicture());
    }

    private byte[] getDefaultProfilePicture(){
        InputStream is = getClass().getResourceAsStream("/images/defaultUser.jpg");
        if (is == null){
            throw new ImageNotFoundException();
        }
        try{
            return is.readAllBytes();
        } catch (Exception e){
            throw new UnreadableFileException();
        }
    }

    @Transactional(readOnly = true)
    @Scheduled(cron = "0 0 12 * * ?")
    @Override
    public void sendMissingDataEmails(){
        for (User user: userDao.getUsersWithPausedBooks()){
            ms.sendMissingDataEmail(user);
        }
    }

    @Transactional
    @Override
    public String fillMissingWriterData(long userId, String password) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        String encodedPassword = passwordEncoder.encode(password);

        userDao.updatePassword(user, encodedPassword);
        userDao.giveRole(user, UserRoles.READER);
        userDao.giveRole(user, UserRoles.WRITER);
        return encodedPassword;
    }
}
