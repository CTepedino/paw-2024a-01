package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

public class UpdateOrderForm {

    private MultipartFile receipt;

    private Boolean approved;

    public MultipartFile getReceipt() {
        return receipt;
    }

    public void setReceipt(MultipartFile receipt) {
        this.receipt = receipt;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

}
