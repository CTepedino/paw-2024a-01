package ar.edu.itba.paw.models;

public enum OrderStatus {
    WAITING_CONTACT("Waiting contact","WAITING_PAYMENT"),
    WAITING_PAYMENT("Waiting payment","WAITING_FOR_BOOK"),
    WAITING_FOR_BOOK("Waiting for book","COMPLETED"),
    COMPLETED("Completed","COMPLETED");

    private final String displayString;
    private final String next;

    OrderStatus(String displayString, String next){
        this.displayString = displayString;
        this.next = next;
    }

    public OrderStatus getNext(){
        return OrderStatus.valueOf(next);
    }

    public String getDisplayString(){
        return displayString;
    }
}

