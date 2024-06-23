package ar.edu.itba.paw.models.users;


import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
public abstract class CodeWithExpiration {

    @Id
    @Column
    private long id;

    @Column(length = 5)
    private String code;

    @Column
    private LocalDateTime expiration;

    CodeWithExpiration(){}

    CodeWithExpiration(long id, String code, LocalDateTime expiration) {
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
