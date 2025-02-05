package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;

import java.util.Locale;
import java.util.Optional;

public interface UserService {

    User create(String email, String password, String firstName, String lastName, Locale locale);

    Optional<User> findById(long userId);
    Optional<User> findByEmail(String email);

    void changePassword(long userId, String password);
    void updateProfile(long userId, String firstName, String lastName, String cbu, String description);
    Optional<User> getLoggedUser();

    void checkWriterRole(User user);
    void checkWriterCategory(User user);

    ProfilePicture getProfilePicture(long userId);
    void updateProfilePicture(long userId, byte[] profilePicture);
    void deleteProfilePicture(long userId);

    void sendResetCode(String email);
    void validateResetPasswordCode(String email, String code);
    boolean isResetPasswordCode(String code);

    void validateEmail(String email, String code);
    void resendValidation(String email);
    boolean isEmailValidationCode(String code);
}