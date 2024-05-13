package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class CreateOrderForm {

    @NotNull
    private MultipartFile receipt;

    public MultipartFile getReceipt() {
        return receipt;
    }

    public void setReceipt(MultipartFile receipt) {
        this.receipt = receipt;
    }
}
