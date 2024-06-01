package ar.edu.itba.paw.models.users;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_validations")
public class EmailValidation {

    @Id
    @Column
    private long id;

    @Column(length = 5)
    private String code;

    @Column
    private LocalDateTime expiration;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id", nullable = false)
    private User user;

    EmailValidation(){}

    public EmailValidation(long id, String code, LocalDateTime expiration) {
        this.id = id;
        this.code = code;
        this.expiration = expiration;
    }

    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public LocalDateTime getExpiration(){
        return expiration;
    }
}


