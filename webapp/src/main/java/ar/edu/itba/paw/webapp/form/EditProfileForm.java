package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.LoggedUserPassword;
import ar.edu.itba.paw.webapp.form.validations.UniqueEmail;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

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


    @Size(min = 22, max = 22)
    @Pattern(regexp = "\\d*")
    private String cbu;



    public MultipartFile getProfilePicture() {
        return profilePicture;
    }

    public String getNewFirstName() {
        return newFirstName;
    }

    public String getNewLastName() {
        return newLastName;
    }


    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
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
