package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.UniqueEmail;

import javax.validation.constraints.*;

public class UserPostDTO {

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    @Email
    @UniqueEmail
    private String email;

    @Size(min=6, max=255)
    private String password;

    @Size(min = 6, max = 22)
    @Pattern(regexp = "[a-zA-ZáéíóúüÁÉÍÓÚÜñÑ0-9.-]+")
    private String cbu;

    @Size(max=500)
    private String description;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCbu() {
        return cbu;
    }

    public void setCbu(String cbu) {
        this.cbu = cbu;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
