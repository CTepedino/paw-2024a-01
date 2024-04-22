package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.OrderDao;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.OrderStatus;
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
            rs.getLong("writer_id"),
            rs.getLong("buyer_id"),
            rs.getLong("book_id"),
            OrderStatus.valueOf(rs.getString("status"))
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public OrderJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("orders");
    }

    @Override
    public Order create(long buyerId, long writerId, long bookId, OrderStatus orderStatus) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("buyer_id", buyerId);
        orderData.put("writer_id", writerId);
        orderData.put("book_id", bookId);
        orderData.put("status", orderStatus);

        simpleJdbcInsert.execute(orderData);

        return new Order(writerId, buyerId, bookId, orderStatus);
    }

    @Override
    public void setStatus(long buyerId, long writerId, long bookId, OrderStatus orderStatus) {
        jdbcTemplate.update(
            """
                UPDATE orders
                SET order_status = ?
                WHERE buyer_id = ? AND writer_id = ? AND book_id = ?
                """,
                orderStatus,
                buyerId,
                writerId,
                bookId
        );
    }

    @Override
    public Optional<Order> find(long buyerId, long writerId, long bookId) {
        List<Order> list = jdbcTemplate.query(
            """
                SELECT *
                FROM orders
                WHERE buyer_id = ? AND writer_id = ? AND book_id = ?
                """,
                ROW_MAPPER,
                buyerId,
                writerId,
                bookId
        );
        return list.stream().findFirst();
    }
}


