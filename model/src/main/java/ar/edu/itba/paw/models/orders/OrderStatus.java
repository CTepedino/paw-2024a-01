package ar.edu.itba.paw.models.orders;

public enum OrderStatus {
    WAITING_PAYMENT(true, false),
    REJECTED_PAYMENT(true, false),
    WAITING_APPROVAL(false, true),
    COMPLETED( false, false);

    private final boolean readerCanAdvance;
    private final boolean writerCanAdvance;

    OrderStatus(boolean readerCanAdvance, boolean writerCanAdvance){
        this.readerCanAdvance = readerCanAdvance;
        this.writerCanAdvance = writerCanAdvance;
    }

    public boolean canReaderAdvance() {
        return readerCanAdvance;
    }

    public boolean canWriterAdvance() {
        return writerCanAdvance;
    }
}

