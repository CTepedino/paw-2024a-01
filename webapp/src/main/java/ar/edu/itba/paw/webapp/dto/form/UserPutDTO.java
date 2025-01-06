package ar.edu.itba.paw.webapp.dto.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserPutDTO {

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String firstName;

    @NotNull
    @NotEmpty
    @Size(max = 255)
    private String lastName;

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
