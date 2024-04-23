package ar.edu.itba.paw.webapp.form;


import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class SignUpForm {

    @NotNull
    @Size(min=1, max=100)
    @Email
    private String email;

    @Size(min=6, max=100)
    private String password;

/*    @Size(min=6, max=100)
    private String repeatPassword;*/

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

/*    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }*/
}
