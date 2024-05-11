package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.UserDao;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {

     final static RowMapper<User> USER_ROW_MAPPER = (rs, rowNum) ->
            new User(
                    rs.getLong("user_id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("cbu"),
                    rs.getBoolean("is_enabled"),
                    Locale.forLanguageTag(rs.getString("locale"))
            );

    private final static RowMapper<UserRoles> ROLE_ROW_MAPPER = (rs, rowNum) -> UserRoles.valueOf(rs.getString("role"));

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert userJdbcInsert;
    private final SimpleJdbcInsert roleJdbcInsert;

    @Autowired
    public UserJdbcDao(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        userJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("user_id")
                .withTableName("users");
        roleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("roles");
    }

    @Override
    public User create(String email, String password, String firstName, String lastName, boolean isEnabled, Locale locale) {

        Map<String, Object> userData = new HashMap<>();
        userData.put("first_name", firstName);
        userData.put("last_name", lastName);
        userData.put("email", email);
        userData.put("password", password);
        userData.put("is_enabled", isEnabled);
        userData.put("locale", locale.toLanguageTag());
        Number generatedId = userJdbcInsert.executeAndReturnKey(userData);

        return new User(
                generatedId.longValue(),
                email,
                password,
                firstName,
                lastName,
                isEnabled,
                locale
        );
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(
        """
                DELETE FROM users
                WHERE user_id = ?
            """,
            id
        );
    }

    @Override
    public int update(long id, String email, String password, String firstName, String lastName, boolean isEnabled) {
        return jdbcTemplate.update(
                """
                UPDATE users
                SET email = ?,
                password = ?,
                first_name = ?,
                last_name = ?,
                is_enabled = ?
                WHERE user_id = ?
            """,
            email, password, firstName, lastName, isEnabled, id
        );
    }

    @Override
    public int update(long id, String email, String password, String firstName, String lastName, String cbu, boolean isEnabled) {
        return jdbcTemplate.update(
        """
                UPDATE users
                SET email = ?,
                password = ?,
                first_name = ?,
                last_name = ?,
                cbu = ?,
                is_enabled = ?
                WHERE user_id = ?
            """, email, password, firstName, lastName, cbu, isEnabled, id
        );
    }

    @Override
    public Optional<User> findById(long id) {
        final List<User> list = jdbcTemplate.query(
            """
                    SELECT *
                    FROM users
                    WHERE user_id = ?
                """,
                USER_ROW_MAPPER,
                id
        );

        return list.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final List<User> list = jdbcTemplate.query(
            """
                    SELECT *
                    FROM users
                    WHERE email = ?
                """,
                USER_ROW_MAPPER,
                email
        );

        return list.stream().findFirst();
    }

    @Override
    public int giveRole(long id, UserRoles role) {
        Map<String, Object> roleData = new HashMap<>();

        roleData.put("user_id", id);
        roleData.put("role", role);

        return roleJdbcInsert.execute(roleData);
    }

    @Override
    public void removeRole(long id, UserRoles role){
        jdbcTemplate.update(
        """
                DELETE FROM roles
                WHERE user_id = ? AND role = ?
            """,
            id,
            role.toString()
        );
    }

    @Override
    public List<UserRoles> getRoles(long id) {
        return jdbcTemplate.query(
            """
                    SELECT *
                    FROM roles
                    WHERE user_id = ?
                """,
                ROLE_ROW_MAPPER,
                id
        );
    }

    @Override
    public void recheckAllPaused(long userId) {
        jdbcTemplate.update(
        """
                UPDATE books b SET is_paused = CASE
                    WHEN NOT EXISTS(
                        SELECT 1
                        FROM book_files bf
                        WHERE bf.id = b.book_id
                    ) OR EXISTS(
                        SELECT 1
                        FROM users AS u
                        WHERE u.user_id = b.writer_id AND u.cbu IS NULL
                    ) THEN TRUE
                    ELSE FALSE
                END WHERE b.writer_id = ?;
            """,
            userId
        );
    }
}

