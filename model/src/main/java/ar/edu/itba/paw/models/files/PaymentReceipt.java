package ar.edu.itba.paw.models.files;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "payment_receipts")
public class PaymentReceipt extends File{

    @Column
    private String type;

    protected PaymentReceipt() {}

    public PaymentReceipt(long receiptId, byte[] receipt, String type){
        super(receiptId, receipt);
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
