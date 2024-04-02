package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserJdbcDao implements UserDao {

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> new User(rs.getLong("user_id"), rs.getString("username"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public UserJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("user_id")
                .withTableName("users");
    }

    @Override
    public Optional<User> findById(long id){
        final List<User> list = jdbcTemplate.query(
                "SELECT * FROM users WHERE user_id = ?",
                new Object[] {id},
                ROW_MAPPER);

        return list.stream().findFirst();
    }

    @Override
    public User create(final String username){
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(userData);
        return new User(generatedId.longValue(), username);
    }
}
