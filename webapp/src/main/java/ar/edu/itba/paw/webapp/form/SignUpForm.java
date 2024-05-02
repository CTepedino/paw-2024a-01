package ar.edu.itba.paw.webapp.form;


import ar.edu.itba.paw.webapp.form.validations.UniqueEmail;

import javax.validation.constraints.*;

public class SignUpForm {

    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    private String lastName;

    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    @Email
    @UniqueEmail
    private String email;

    @Size(min=6, max=255)
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
