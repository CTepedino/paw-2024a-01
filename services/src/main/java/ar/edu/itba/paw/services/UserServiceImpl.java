package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.exception.*;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.users.WriterCategory;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final EmailValidationService evs;
    private final ResetCodeService rcs;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(final UserDao userDao, OrderDao orderDao, PasswordEncoder passwordEncoder, EmailValidationService evs, ResetCodeService rcs){
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.passwordEncoder = passwordEncoder;
        this.evs = evs;
        this.rcs = rcs;
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
    public User create(String email, String password, String firstName, String lastName, Locale locale){
        User user = userDao.create(
                email,
                passwordEncoder.encode(password),
                firstName,
                lastName,
                false,
                locale
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
    public void checkWriterRole(User user) {
        if (user.getCbu()==null){
            throw new InvalidWriterException();
        }
        if (!user.getRoles().contains(UserRoles.WRITER)) {
            userDao.giveRole(user, UserRoles.WRITER);
        }

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
    public void changePassword(long userId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        userDao.updatePassword(user, encodedPassword);

        LOGGER.atDebug().setMessage("Changed password for userId: {}").addArgument(user.getUserId()).log();
    }

    @Transactional
    @Override
    public void updateProfile(long userId, String firstName, String lastName, String cbu, String description) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        userDao.update(user, firstName, lastName, cbu, description);

        LOGGER.atDebug().setMessage("Updated profile for user: {}").addArgument(firstName).log();
    }

    @Transactional
    @Override
    public void updateProfilePicture(long userId, byte[] profilePicture) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);

        if (user.getProfilePicture()==null){
            userDao.createProfilePicture(user, profilePicture);
        } else {
            userDao.updateProfilePicture(user, profilePicture);
        }

        LOGGER.atDebug().setMessage("Updated profile picture for user: {}").addArgument(user.getFirstName()).log();
    }

    @Transactional
    @Override
    public void deleteProfilePicture(long userId) {
        Optional<User> maybeUser = userDao.findById(userId);
        if (maybeUser.isPresent()){
            if (maybeUser.get().getProfilePicture() != null){
                userDao.deleteProfilePicture(maybeUser.get());
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<UserRoles> getRoles(long userId){
        return findById(userId).orElseThrow(UserNotFoundException::new).getRoles();
    }


    @Transactional(readOnly = true)
    @Override
    public ProfilePicture getProfilePicture(long userId){
        ProfilePicture image = findById(userId).orElseThrow(UserNotFoundException::new).getProfilePicture();
        if (image == null){
            return new ProfilePicture(userId, getDefaultProfilePicture());
        }
        return image;
    }

    private byte[] getDefaultProfilePicture(){
        InputStream is = getClass().getResourceAsStream("/images/defaultUser.jpg");
        if (is == null){
            throw new ImageNotFoundException();
        }
        try {
            return is.readAllBytes();
        } catch (IOException | OutOfMemoryError e){
            throw new UnreadableFileException();
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

    @Transactional
    @Override
    public void createResetPasswordCode(String email) {
        User user = findByEmail(email).orElseThrow(UserNotFoundException::new);
        rcs.create(user);
    }


    @Transactional
    @Override
    public void resetPassword(long userId, String password, String code) {
        User user = findById(userId).orElseThrow(UserNotFoundException::new);

        if (rcs.checkResetCode(userId, code)) {
            userDao.updatePassword(user, passwordEncoder.encode(password));

            List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.toString())).toList();
            Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            throw new InvalidCodeException();
        }
    }

    @Transactional(readOnly = true)
    @Override
    public void resendResetCode(long userId){
        rcs.resend(findById(userId).orElseThrow(UserNotFoundException::new));
    }

    @Transactional
    @Override
    public void checkWriterCategory(User user){
        long orders = orderDao.getWriterOrdersSize(user.getUserId(), "", null);
        if(user.getWriterCategory() != WriterCategory.BRONZE && orders >= WriterCategory.BRONZE.getMinSales() && orders < WriterCategory.SILVER.getMinSales()){
            userDao.updateWriterCategory(user, WriterCategory.BRONZE);
        }
        if((user.getWriterCategory() != WriterCategory.SILVER) && (orders >= WriterCategory.SILVER.getMinSales()) && (orders < WriterCategory.GOLD.getMinSales())){
            userDao.updateWriterCategory(user, WriterCategory.SILVER);
        }
        if(user.getWriterCategory() != WriterCategory.GOLD && orders >= WriterCategory.GOLD.getMinSales()){
            userDao.updateWriterCategory(user, WriterCategory.GOLD);
        }
    }
}