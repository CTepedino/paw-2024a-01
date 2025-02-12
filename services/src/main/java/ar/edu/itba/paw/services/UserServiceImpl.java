package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.interfaces.service.EmailValidationService;
import ar.edu.itba.paw.interfaces.service.ResetCodeService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.*;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.users.WriterCategory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final OrderDao orderDao;

    private final EmailValidationService evs;
    private final ResetCodeService rcs;

    private final PasswordEncoder passwordEncoder;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(final UserDao userDao, OrderDao orderDao, PasswordEncoder passwordEncoder, EmailValidationService evs, ResetCodeService rcs){
        this.userDao = userDao;
        this.orderDao = orderDao;
        this.passwordEncoder = passwordEncoder;
        this.evs = evs;
        this.rcs = rcs;
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

    @Transactional(readOnly = true)
    @Override
    public Optional<User> getLoggedUser(){
        if (SecurityContextHolder.getContext() == null || SecurityContextHolder.getContext().getAuthentication() == null){
            return Optional.empty();
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return findByEmail(auth.getName());
    }

    @Transactional
    @Override
    public void changePassword(long userId, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        User user = findById(userId).orElseThrow(UserNotFoundException::new);
        userDao.updatePassword(user, encodedPassword);
        rcs.deleteCode(userId);

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
    public void checkWriterRole(User user) {
        if (user.getCbu()==null){
            throw new InvalidWriterException();
        }

        if (!user.getRoles().contains(UserRoles.WRITER)) {
            userDao.giveRole(user, UserRoles.WRITER);
        }

        LOGGER.atDebug().setMessage("Gave writer role to userId: {}").addArgument(user.getUserId()).log();
    }

    @Transactional
    @Override
    public void checkWriterCategory(User user){
        long orders = orderDao.getAllOrdersSize(null, user.getUserId(), null, "", OrderStatus.COMPLETED);

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
        User user = userDao.findById(userId).orElseThrow(UserNotFoundException::new);
        if (user.getProfilePicture() != null){
            userDao.deleteProfilePicture(user);
        }

        LOGGER.atDebug().setMessage("Deleted profile picture for user: {}").addArgument(user.getFirstName()).log();
    }

    @Transactional
    @Override
    public void sendResetCode(String email) {
        User user = findByEmail(email).orElseThrow(BadRequestException::new);
        if (user.getResetCode() == null){
            rcs.create(user);
        } else {
            rcs.resend(user);
        }
    }

    @Transactional
    @Override
    public void validateResetPasswordCode(String email, String code) {
        User user = findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (rcs.checkResetCode(user.getUserId(), code)) {
            List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.toString())).toList();
            Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            throw new InvalidCodeException();
        }
    }

    @Override
    public boolean isResetPasswordCode(String code){
        return rcs.isResetPasswordCode(code);
    }

    @Transactional
    @Override
    public void validateEmail(String email, String code) {
        Optional<User> maybeUser = userDao.findByEmail(email);
        if (maybeUser.isPresent() && !maybeUser.get().isEnabled()){
            User user = maybeUser.get();
            if (evs.checkValidation(user.getUserId(), code)){

                userDao.updateIsEnabled(user,true);
                userDao.giveRole(user, UserRoles.READER);

                List<SimpleGrantedAuthority> authorities = user.getRoles().stream().map(p -> new SimpleGrantedAuthority(p.toString())).toList();
                Authentication auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                LOGGER.atWarn().setMessage("Failed to Validate email for user: {} - Invalid Code").addArgument(email).log();
                throw new InvalidCodeException();
            }
        } else {
            LOGGER.atWarn().setMessage("Failed to Validated email for user: {} - No validation code").addArgument(email).log();
            throw new NoValidationCodeException();
        }
        LOGGER.atDebug().setMessage("Validated email for user: {}").addArgument(email).log();
    }

    @Transactional
    @Override
    public void resendValidation(String email) {
        User user = userDao.findByEmail(email).orElseThrow(BadRequestException::new);
        evs.resend(user);
    }

    @Override
    public boolean isEmailValidationCode(String code) {
        return evs.isEmailValidationCode(code);
    }




}