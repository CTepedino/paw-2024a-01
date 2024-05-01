package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.webapp.form.validations.UniqueEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class SignUpForm {

    @Size(min = 1, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String firstName;

    @Size(min = 1, max = 100)
    @Pattern(regexp = "[a-zA-Z0-9]+")
    private String lastName;

    @NotNull
    @Size(min=1, max=100)
    @Email
    @UniqueEmail
    private String email;

    @Size(min=6, max=100)
    private String password;

    private String repeatPassword;

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

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
}
