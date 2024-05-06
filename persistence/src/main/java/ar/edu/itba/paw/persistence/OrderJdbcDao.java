package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class OrderJdbcDao implements OrderDao {


    private final static RowMapper<Order> ROW_MAPPER = (rs, rowNum) -> new Order(
            new User(
                rs.getLong("r_user_id"),
                rs.getString("r_email"),
                rs.getString("r_password"),
                rs.getString("r_first_name"),
                rs.getString("r_last_name")
            ),
            BookJdbcDao.ROW_MAPPER.mapRow(rs, rowNum),
            OrderStatus.valueOf(rs.getString("status")),
            rs.getTimestamp("date").toLocalDateTime());


    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public OrderJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("orders")
                .usingColumns("buyer_id","book_id","status");
    }

    @Override
    public void create(long buyerId, long bookId, OrderStatus orderStatus) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("buyer_id", buyerId);
        orderData.put("book_id", bookId);
        orderData.put("status", orderStatus);

        simpleJdbcInsert.execute(orderData);
    }

    @Override
    public void setStatus(long buyerId, long bookId, OrderStatus orderStatus) {
        jdbcTemplate.update(
            """
                UPDATE orders
                SET status = ?
                WHERE buyer_id = ? AND book_id = ?
                """,
                orderStatus.toString(),
                buyerId,
                bookId
        );
    }

    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        List<Order> list = jdbcTemplate.query(
            """
                SELECT o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name
                FROM orders o
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                JOIN users w ON b.writer_id = w.user_id
                WHERE o.buyer_id = ? AND o.book_id = ?
                """,
                ROW_MAPPER,
                buyerId,
                bookId
        );
        return list.stream().findFirst();
    }

    @Override
    public List<Order> getAllReaderOrders(long readerId) {
        return jdbcTemplate.query(
            """
                SELECT o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name
                FROM orders o
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                JOIN users w ON b.writer_id = w.user_id
                WHERE o.buyer_id = ?
                """,
                ROW_MAPPER,
                readerId
        );
    }

    @Override
    public List<Order> getAllWriterOrders(long writerId) {
        return jdbcTemplate.query(
                """
                SELECT o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name
                FROM orders o
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                JOIN users w ON b.writer_id = w.user_id
                WHERE w.user_id = ?
                    """,
                ROW_MAPPER,
                writerId
        );
    }

}


