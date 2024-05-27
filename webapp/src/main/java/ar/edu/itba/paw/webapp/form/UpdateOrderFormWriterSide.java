package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.FileExists;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class UpdateOrderFormWriterSide {

    private Boolean approved;

    @NotNull
    @Size(max = 500)
    private String reason;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
