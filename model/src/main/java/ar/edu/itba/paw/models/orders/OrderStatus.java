package ar.edu.itba.paw.models.orders;

public enum OrderStatus {
    WAITING_CONTACT("Waiting contact","WAITING_PAYMENT", false, true),
    WAITING_PAYMENT("Waiting payment","WAITING_FOR_BOOK", true, false),
    WAITING_FOR_BOOK("Waiting for book","COMPLETED", false, true),
    COMPLETED("Completed","COMPLETED", false, false);

    private final String displayString;
    private final String next;
    private final boolean readerCanAdvance;
    private final boolean writerCanAdvance;

    OrderStatus(String displayString, String next, boolean readerCanAdvance, boolean writerCanAdvance){
        this.displayString = displayString;
        this.next = next;
        this.readerCanAdvance = readerCanAdvance;
        this.writerCanAdvance = writerCanAdvance;
    }

    public OrderStatus getNext(){
        return OrderStatus.valueOf(next);
    }

    public String getDisplayString(){
        return displayString;
    }

    public boolean getReaderCanAdvance() {
        return readerCanAdvance;
    }

    public boolean getWriterCanAdvance() {
        return writerCanAdvance;
    }
}

