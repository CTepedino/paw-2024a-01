package ar.edu.itba.paw.interfaces.dao;

import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;

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

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    void giveRole(User user, UserRoles role);


    //TODO: mover a BookDao?
    void recheckAllPaused(long userId);

    List<User> getUsersWithPausedBooks();
}
