package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.FileExists;
import org.springframework.web.multipart.MultipartFile;

public class UpdateOrderFormWriterSide {

    private Boolean approved;

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

}
