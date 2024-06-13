package ar.edu.itba.paw.models.books;

import ar.edu.itba.paw.models.deals.Deal;

public class BookAndDeal {

    private final Book book;
    private final Deal deal;

    public BookAndDeal(Book book, Deal deal){
        this.book=book;
        this.deal=deal;
    }

    public Book getBook(){
        return book;
    }

    public Deal getDeal(){
        return deal;
    }
}
