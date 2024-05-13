package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.FileExists;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

public class CreateOrderForm {

    @FileExists
    private MultipartFile receipt;

    public MultipartFile getReceipt() {
        return receipt;
    }

    public void setReceipt(MultipartFile receipt) {
        this.receipt = receipt;
    }
}
