package ar.edu.itba.paw.webapp.dto.input;

import javax.validation.constraints.Size;

public class OrderEditDTO {

    @Size(max = 255)
    private String rejectionReason;

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }
}
