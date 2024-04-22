package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRoles;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class UserJdbcDao implements UserDao {

    private final static RowMapper<User> ROW_MAPPER = (rs, rowNum) -> {

        Object[] roles = (Object[]) rs.getArray("roles").getArray();
        List<UserRoles> list = new ArrayList<>();

        for (Object roleString : roles) {
            if (roleString != null) {
                list.add(UserRoles.valueOf((String) roleString));
            }
        }


        return new User(
                rs.getLong("user_id"),
                list.toArray(new UserRoles[0]),
                rs.getString("first_name"),
                rs.getString("last_name"),
                rs.getString("email"),
                rs.getString("password")
        );
    };


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert userJdbcInsert;
    private final SimpleJdbcInsert roleJdbcInsert;

    @Autowired
    public UserJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        userJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("user_id")
                .withTableName("users");
        roleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("roles");
    }

    @Override
    public User create(UserRoles[] roles, String firstName, String lastName, String email, String password) {

        Map<String, Object> userData = new HashMap<>();
        userData.put("first_name", firstName);
        userData.put("last_name", lastName);
        userData.put("email", email);
        userData.put("password", password);

        Number generatedId = userJdbcInsert.executeAndReturnKey(userData);

        Map<String, Object> roleData;
        for (UserRoles role : roles){
            roleData = new HashMap<>();
            roleData.put("user_id", generatedId);
            roleData.put("role", role);
            roleJdbcInsert.execute(roleData);
        }

        return new User(
                generatedId.longValue(),
                roles,
                firstName,
                lastName,
                email,
                password
        );
    }

    @Override
    public Optional<User> findById(long id) {
        final List<User> list = jdbcTemplate.query(
                """
                    SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, array_agg(r.role) AS roles
                    FROM users u LEFT JOIN roles r ON u.user_id = r.user_id
                    WHERE u.user_id = ?
                    GROUP BY u.user_id, u.first_name, u.last_name, u.email, u.password
                    """,
                ROW_MAPPER,
                id
        );

        return list.stream().findFirst();
    }

    @Override
    public User giveRole(long id, UserRoles role) {

        Optional<User> user = findById(id);
        if (user.isEmpty()){
            throw new UserNotFoundException();
        }

        Map<String, Object> roleData = new HashMap<>();

        roleData.put("user_id", id);
        roleData.put("role", role);

        roleJdbcInsert.execute(roleData);

        UserRoles[] roles = Arrays.copyOf(user.get().getRoles(), user.get().getRoles().length + 1);
        roles[roles.length-1] = role;

        return new User(
                id,
                roles,
                user.get().getFirstName(),
                user.get().getLastName(),
                user.get().getEmail(),
                user.get().getPassword()
        );

    }

    @Override
    public Optional<User> findByEmail(String email) {

        List<User> list = jdbcTemplate.query(
                """
                    SELECT u.user_id, u.first_name, u.last_name, u.email, u.password, array_agg(r.role) AS roles
                    FROM users u LEFT JOIN roles r ON u.user_id = r.user_id
                    WHERE u.email = ?
                    GROUP BY u.user_id, u.first_name, u.last_name, u.email, u.password
                    """,
                ROW_MAPPER,
                email
        );

        return list.stream().findFirst();
    }
}
