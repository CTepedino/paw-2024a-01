package ar.edu.itba.paw.models.files;

public class PaymentReceipt extends File{

    private final String type;

    public PaymentReceipt(long receiptId, byte[] receipt, String type){
        super(receiptId, receipt);
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
