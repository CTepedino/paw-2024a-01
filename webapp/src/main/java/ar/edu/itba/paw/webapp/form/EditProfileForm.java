package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.ImageFile;
import ar.edu.itba.paw.webapp.form.validations.LoggedUserPassword;
import ar.edu.itba.paw.webapp.form.validations.UniqueEmail;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;

public class EditProfileForm {

    @NotNull
    @ImageFile
    private MultipartFile picture;


    @NotNull
    @NotEmpty
    @Size(max = 50, message = "MaxSize.regexp")
    private String newFirstName;

    @NotNull
    @NotEmpty
    @Size(max = 50, message = "MaxSize.regexp")
    private String newLastName;


    @Size(min = 6, max = 22)
    @Pattern(regexp = "[a-zA-ZáéíóúüÁÉÍÓÚÜñÑ0-9.-]+")
    private String cbu;

    @Size(max=500)
    private String description;


    public MultipartFile getPicture() {
        return picture;
    }

    public void setPicture(MultipartFile picture) {
        this.picture = picture;
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


    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
