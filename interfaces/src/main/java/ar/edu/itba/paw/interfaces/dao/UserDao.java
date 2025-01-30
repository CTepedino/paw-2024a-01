package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface UserDao {
    User create(String email, String password, String firstName, String lastName, boolean isEnabled, Locale locale);

    void update(User user, String firstName, String lastName, String cbu, String description);
    void updateIsEnabled(User user, boolean enabled);
    void updatePassword(User user, String password);

    ProfilePicture createProfilePicture(User user, byte[] profilePicture);
    void updateProfilePicture(User user, byte[] profilePicture);
    void deleteProfilePicture(User user);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    void giveRole(User user, UserRoles role);


    List<User> getUsersWithPausedBooks();

    EmailValidation createEmailValidation(long id, String code, LocalDateTime expiration);
    void deleteExpiredEmailValidations();
    void deleteEmailValidation(long id);

    ResetCode createResetCode(long id, String code, LocalDateTime expiration);
    void deleteExpiredResetCodes();
    void deleteResetCode(long id);

    void updateWriterCategory(User user, WriterCategory writerCategory);
}