package ar.edu.itba.paw.interfaces.service;

import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    User create(String email, String password, String firstName, String lastName);

    Optional<User> findById(long id);

    Optional<User> findByEmail(String email);

    List<UserRoles> getRoles(long id);

    void giveWriterRole(long id, String cbu);

/*    void fillMissingWriterData(long id, String password);*/

    boolean isCurrentUserPassword(String password);
    void changePassword(String password);
    void updateProfile(String firstName, String lastName, String CBU);

    Optional<User> getLoggedUser();

    boolean isLoggedIn();

    void setProfilePicture(MultipartFile profilePicture);
    ProfilePicture getProfilePictureOrDefault(long id);
}
