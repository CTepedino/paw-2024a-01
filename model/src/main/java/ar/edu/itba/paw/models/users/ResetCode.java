package ar.edu.itba.paw.models.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "reset_codes")
public class ResetCode extends CodeWithExpiration {

    ResetCode(){}

    public ResetCode(final long id, final String code, final LocalDateTime expiration) {
        super(id, code, expiration);
    }
}
