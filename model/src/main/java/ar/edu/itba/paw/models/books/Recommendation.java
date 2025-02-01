package ar.edu.itba.paw.models.books;

import javax.persistence.*;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recommendations_id_seq")
    @SequenceGenerator(sequenceName = "recommendations_id_seq", name = "recommendations_id_seq", allocationSize = 1)
    @Column
    private Long id;

    @Column(name = "user_id")
    private long userId;

    @Column(name = "book_id")
    private long bookId;

    Recommendation(){}

    public Recommendation(long userId, long bookId){
        this.userId = userId;
        this.bookId = bookId;
    }

    public Long getId() {
        return id;
    }

    public long getUserId() {
        return userId;
    }


    public long getBookId() {
        return bookId;
    }
}
