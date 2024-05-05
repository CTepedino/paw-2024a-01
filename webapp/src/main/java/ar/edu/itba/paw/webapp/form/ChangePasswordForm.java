package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.LoggedUserPassword;

import javax.validation.constraints.Size;

public class ChangePasswordForm {

    @LoggedUserPassword
    private String oldPassword;

    @Size(min=6, max=255)
    private String password;

    private String repeatPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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
