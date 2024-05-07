package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.LoggedUserPassword;
import ar.edu.itba.paw.webapp.form.validations.UniqueEmail;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class EditProfileForm {



    @NotNull
    private MultipartFile profilePicture;


    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    private String newFirstName;

    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    private String newLastName;

    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    @Email
    @UniqueEmail
    private String newEmail;

    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public String getNewEmail() {
        return newEmail;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }

    public void setNewEmail(String newEmail) {
        this.newEmail = newEmail;
    }


    public void setNewFirstName(String newFirstName) {
        this.newFirstName = newFirstName;
    }

    public void setNewLastName(String newLastName) {
        this.newLastName = newLastName;
    }

    public void setProfilePicture(MultipartFile profilePicture) {
        this.profilePicture = profilePicture;
    }

}
