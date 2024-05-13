package ar.edu.itba.paw.models.files;

public class PaymentReceipt extends File{
    public PaymentReceipt(long receiptId, byte[] receipt){
        super(FileType.PDF, receiptId, receipt);
    }
}
