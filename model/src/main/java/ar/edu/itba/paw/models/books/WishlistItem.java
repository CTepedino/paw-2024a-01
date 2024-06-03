package ar.edu.itba.paw.models.books;


import ar.edu.itba.paw.models.users.User;

import javax.persistence.*;

@Entity
@Table(name = "wishlist")
public class WishlistItem {


    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wishlist_id_seq")
    @SequenceGenerator(sequenceName = "wishlist_id_seq", name = "wishlist_id_seq", allocationSize = 1)
    @Column
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "book_id")
    private long bookId;

    WishlistItem(){}

    public WishlistItem(long userId, long bookId){
        this.userId = userId;
        this.bookId = bookId;
    }

}
