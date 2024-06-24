package ar.edu.itba.paw.models.orders;

public enum OrderStatus {
    REJECTED_PAYMENT(true, false),
    WAITING_APPROVAL(false, true),
    COMPLETED( false, false);

    private final boolean readerCanAdvance;
    private final boolean writerCanAdvance;

    OrderStatus(boolean readerCanAdvance, boolean writerCanAdvance){
        this.readerCanAdvance = readerCanAdvance;
        this.writerCanAdvance = writerCanAdvance;
    }

    public boolean getReaderCanAdvance() {
        return readerCanAdvance;
    }

    public boolean getWriterCanAdvance() {
        return writerCanAdvance;
    }
}

