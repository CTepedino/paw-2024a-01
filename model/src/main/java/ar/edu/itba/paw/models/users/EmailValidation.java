package ar.edu.itba.paw.models.users;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_validations")
public class EmailValidation extends CodeWithExpiration{

    EmailValidation(){}

    public EmailValidation(long id, String code, LocalDateTime expiration) {
        super(id, code, expiration);
    }

}


