package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;

import java.io.File;
import java.io.InputStream;
import java.util.Collection;
import java.util.Locale;
import java.util.Optional;

public interface UserService {

    User create(String email, String password, String firstName, String lastName, Locale locale);

    void validateEmail(long userId, String code);

    void resendValidation(String email);

    Optional<User> findById(long userId);

    Optional<User> findByEmail(String email);

    void checkWriterRole(User user);

    boolean isCurrentUserPassword(String password);
    void changePassword(long userId, String password);

    void updateProfile(long userId, String firstName, String lastName, String cbu, String description);



    Optional<User> getLoggedUser();

    boolean isLoggedIn();

    boolean hasRole(UserRoles role);

    Collection<UserRoles> getRoles(long userId);

    ProfilePicture getProfilePicture(long userId);
    void updateProfilePicture(long userId, byte[] profilePicture);
    void deleteProfilePicture(long userId);

    String fillMissingWriterData(long userId, String password);

    void createResetPasswordCode(String email);

    void resetPassword(long userId, String password, String code);

    void resendResetCode(long userId);

    void checkWriterCategory(User user);
}