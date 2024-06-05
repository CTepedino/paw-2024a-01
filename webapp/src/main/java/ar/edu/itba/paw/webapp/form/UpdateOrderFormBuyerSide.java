package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.form.validations.FileExists;
import ar.edu.itba.paw.webapp.form.validations.PdfOrImageFile;
import org.springframework.web.multipart.MultipartFile;

public class UpdateOrderFormBuyerSide {

    @FileExists
    @PdfOrImageFile
    private MultipartFile receipt;

    public MultipartFile getReceipt() {
        return receipt;
    }

    public void setReceipt(MultipartFile receipt) {
        this.receipt = receipt;
    }
}
