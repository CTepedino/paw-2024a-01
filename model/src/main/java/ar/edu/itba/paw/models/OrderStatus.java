package ar.edu.itba.paw.models;

public enum OrderStatus {
    WAITING_CONTACT("Waiting contact","WAITING_PAYMENT", true, false),
    WAITING_PAYMENT("Waiting payment","WAITING_FOR_BOOK", false, true),
    WAITING_FOR_BOOK("Waiting for book","COMPLETED", true, false),
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

