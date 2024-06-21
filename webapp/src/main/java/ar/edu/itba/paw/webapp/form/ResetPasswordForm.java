package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ResetPasswordForm {

    @Size(min=6, max=255)
    private String password;

    private String repeatPassword;


    public @Size(min = 6, max = 255) String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6, max = 255) String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

}
