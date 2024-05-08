package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.EmailValidationDao;
import ar.edu.itba.paw.models.users.EmailValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EmailValidationJdbcDao implements EmailValidationDao {

    private static final RowMapper<EmailValidation> ROW_MAPPER = (rs, rowNum) -> new EmailValidation(rs.getString("email"), rs.getString("code"), rs.getTimestamp("expiration").toLocalDateTime());

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public EmailValidationJdbcDao(final DataSource ds) {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("email_validations");
    }

    @Override
    public void create(long id, String code, LocalDateTime expiration) {
        Map<String, Object> validationData = new HashMap<>();
        validationData.put("id", id);
        validationData.put("code", code);
        validationData.put("expiration", Timestamp.valueOf(expiration));
        simpleJdbcInsert.execute(validationData);
    }

    @Override
    public Optional<EmailValidation> get(long id) {
        List<EmailValidation> list = jdbcTemplate.query(
            """
                    SELECT v.code, u.email
                    FROM email_validations v JOIN users u on v.id = u.user_id
                    WHERE id = ?
                """,
                ROW_MAPPER,
                id
        );
        return list.stream().findFirst();
    }

    @Override
    public void deleteExpired() {
        jdbcTemplate.update(
        """
                DELETE FROM users
                WHERE EXISTS (
                    SELECT 1
                    FROM email_validations
                    WHERE users.user_id = email_validations.id
                    AND email_validations.expiration < now()
                );
            """
        );
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(
        """
                DELETE FROM email_validations
                WHERE id = ?
            """,
            id
        );
    }
}
