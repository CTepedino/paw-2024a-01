package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface UserService {

    User create(String email, String password, String firstName, String lastName);

    void validateEmail(long id, String code);

    void resendValidation(String email);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    void giveWriterRole(User user, String cbu);

    boolean isCurrentUserPassword(String password);
    void changePassword(String password);
    void updateProfile(String firstName, String lastName, String cbu, MultipartFile profilePicture, String description);

    Optional<User> getLoggedUser();

    boolean isLoggedIn();

    boolean hasRole(UserRoles role);

    ProfilePicture getProfilePictureOrDefault(long id);

    void sendMissingDataEmails();

    String fillMissingWriterData(User user, String password);
}
