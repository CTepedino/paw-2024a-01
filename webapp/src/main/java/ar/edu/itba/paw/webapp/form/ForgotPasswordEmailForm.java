package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.ExistingEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ForgotPasswordEmailForm {

    @NotNull
    @NotEmpty
    @Size(max = 255, message = "MaxSize.regexp")
    @Email
    @ExistingEmail
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
