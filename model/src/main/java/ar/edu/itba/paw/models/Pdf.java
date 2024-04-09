package ar.edu.itba.paw.models;

public class Pdf {
    private final long pdfId;
    private final byte[] pdf;

    public Pdf(long pdfId, byte[] pdf) {
        this.pdfId = pdfId;
        this.pdf = pdf;
    }

    public long getPdfId() {
        return pdfId;
    }

    public byte[] getPdf() {
        return pdf;
    }
}
