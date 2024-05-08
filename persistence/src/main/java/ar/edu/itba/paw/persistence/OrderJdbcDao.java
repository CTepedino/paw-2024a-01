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
            rs.getLong("order_id"),
            new User(
                rs.getLong("r_user_id"),
                rs.getString("r_email"),
                rs.getString("r_password"),
                rs.getString("r_first_name"),
                rs.getString("r_last_name"),
                rs.getBoolean("r_is_enabled")
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
                .usingGeneratedKeyColumns("order_id")
                .usingColumns("buyer_id","book_id","status");
    }

    @Override
    public long create(long buyerId, long bookId, OrderStatus orderStatus) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("buyer_id", buyerId);
        orderData.put("book_id", bookId);
        orderData.put("status", orderStatus);

        return simpleJdbcInsert.executeAndReturnKey(orderData).longValue();
    }

    @Override
    public void setStatus(long orderId, OrderStatus orderStatus) {
        jdbcTemplate.update(
            """
                UPDATE orders
                SET status = ?
                WHERE order_id = ?
                """,
                orderStatus.toString(),
                orderId
        );
    }

    @Override
    public Optional<Order> find(long buyerId, long bookId) {
        List<Order> list = jdbcTemplate.query(
            """
                SELECT o.order_id, o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name, r.is_enabled AS r_is_enabled
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
    public Optional<Order> findById(long orderId){
        List<Order> list = jdbcTemplate.query(
                """
                    SELECT o.order_id, o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name, r.is_enabled AS r_is_enabled
                    FROM orders o
                    JOIN users r ON o.buyer_id = r.user_id
                    JOIN books b ON o.book_id = b.book_id
                    JOIN users w ON b.writer_id = w.user_id
                    WHERE o.order_id = ?
                    """,
                ROW_MAPPER,
                orderId
        );
        return list.stream().findFirst();
    }

    @Override
    public List<Order> getAllReaderOrders(long readerId, int offset, int limit) {
        return jdbcTemplate.query(
        """
                SELECT o.order_id, o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name, r.is_enabled AS r_is_enabled
                FROM orders o
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                JOIN users w ON b.writer_id = w.user_id
                WHERE o.buyer_id = ?
                OFFSET ? LIMIT ?
            """,
            ROW_MAPPER,
            readerId,
            offset,
            limit
        );
    }

    @Override
    public List<Order> getReaderOrdersWithParams(long readerId,  String title, OrderStatus orderStatus, int offset, int limit) {

        StringBuilder query = new StringBuilder("""
                SELECT o.order_id, o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name
                        FROM orders o
                        JOIN users r ON o.buyer_id = r.user_id
                        JOIN books b ON o.book_id = b.book_id
                        JOIN users w ON b.writer_id = w.user_id
                        WHERE o.buyer_id = ?
                """);
        List<Object> params = new ArrayList<>();
        params.add(readerId);
        query.append("AND lower(title) LIKE lower(?) ");
        params.add("%" + (title!=null?title:"") + "%");
        if (orderStatus!=null) {
            DaoUtils.addQueryCondition(query, params, " AND status = ? ", orderStatus.toString());
        }
        query.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        return jdbcTemplate.query(query.toString(), ROW_MAPPER, params.toArray());
    }

    @Override
    public long getAllReaderOrdersSize(long readerId) {
        return DaoUtils.getRowCount(
                jdbcTemplate,
                "orders",
                "WHERE buyer_id = ?",
                readerId
        );
    }

    @Override
    public List<Order> getAllWriterOrders(long writerId, int offset, int limit) {
        return jdbcTemplate.query(
        """
                SELECT o.order_id, o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name, r.is_enabled AS r_is_enabled
                FROM orders o
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                JOIN users w ON b.writer_id = w.user_id
                WHERE w.user_id = ?
                OFFSET ? LIMIT ?
            """,
            ROW_MAPPER,
            writerId,
            offset,
            limit
        );
    }

    @Override
    public long getAllWriterOrdersSize(long writerId) {
        return DaoUtils.getRowCount(
                jdbcTemplate,
                "orders o JOIN books b ON o.book_id = b.book_id",
                "WHERE b.writer_id = ?",
                writerId
        );
    }

    @Override
    public List<Order> getWriterOrdersWithParams(long writerId,  String title, OrderStatus orderStatus, int offset, int limit) {

        StringBuilder query = new StringBuilder("""
                SELECT o.order_id, o.status, o.date, b.*, w.*, r.user_id AS r_user_id,r.email AS r_email, r.password AS r_password, r.first_name AS r_first_name, r.last_name AS r_last_name
                FROM orders o
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                JOIN users w ON b.writer_id = w.user_id
                WHERE w.user_id = ?
                """);
        List<Object> params = new ArrayList<>();
        params.add(writerId);
        query.append("AND lower(title) LIKE lower(?) ");
        params.add("%" + (title!=null?title:"") + "%");
        if (orderStatus!=null) {
            DaoUtils.addQueryCondition(query, params, " AND status = ? ", orderStatus.toString());
        }
        query.append(" LIMIT ? OFFSET ?");
        params.add(limit);
        params.add(offset);
        return jdbcTemplate.query(query.toString(), ROW_MAPPER, params.toArray());
    }

}

