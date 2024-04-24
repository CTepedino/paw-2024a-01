package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.OrderDao;
import ar.edu.itba.paw.models.*;
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
            new PublicUserInformation(
                    rs.getLong("writer_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email")
            ),
            new PublicUserInformation(
                    rs.getLong("buyer_id"),
                    null,
                    null,
                    rs.getString("reader_email")
            ),
            new Book(
                    rs.getLong("book_id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    BookGenre.valueOf(rs.getString("genre")),
                    rs.getDouble("price"),
                    rs.getInt("page_count"),
                    rs.getLong("pdf_id"),
                    rs.getLong("image_id"),
                    rs.getInt("suggested_age"),
                    rs.getDate("published_date"),
                    new PublicUserInformation(
                            rs.getLong("writer_id"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            rs.getString("email")
                    )
            ),

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
    public void create(long buyerId, long writerId, long bookId, OrderStatus orderStatus) {
        Map<String, Object> orderData = new HashMap<>();
        orderData.put("buyer_id", buyerId);
        orderData.put("writer_id", writerId);
        orderData.put("book_id", bookId);
        orderData.put("status", orderStatus);

        simpleJdbcInsert.execute(orderData);
    }

    @Override
    public void setStatus(long buyerId, long writerId, long bookId, OrderStatus orderStatus) {
        jdbcTemplate.update(
            """
                UPDATE orders
                SET status = ?
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
                SELECT o.*, b.*, w.*, r.email AS reader_email
                FROM orders o
                JOIN users w ON o.writer_id = w.user_id
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                WHERE o.buyer_id = ? AND o.writer_id = ? AND o.book_id = ?
                """,
                ROW_MAPPER,
                buyerId,
                writerId,
                bookId
        );
        return list.stream().findFirst();
    }

    @Override
    public List<Order> getAllReaderOrders(long readerId) {
        return jdbcTemplate.query(
            """
                SELECT o.*, b.*, w.*, r.email AS reader_email
                FROM orders o
                JOIN users w ON o.writer_id = w.user_id
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
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
                SELECT o.*, b.*, w.*, r.email AS reader_email
                FROM orders o
                JOIN users w ON o.writer_id = w.user_id
                JOIN users r ON o.buyer_id = r.user_id
                JOIN books b ON o.book_id = b.book_id
                WHERE o.writer_id = ?
                    """,
                ROW_MAPPER,
                writerId
        );
    }

    @Override
    public List<Order> getAllNonCompleteReaderOrders(long readerId) {
        return jdbcTemplate.query(
                """
                    SELECT o.*, b.*, w.*, r.email AS reader_email
                    FROM orders o
                    JOIN users w ON o.writer_id = w.user_id
                    JOIN users r ON o.buyer_id = r.user_id
                    JOIN books b ON o.book_id = b.book_id
                    WHERE o.buyer_id = ? AND o.status <> 'COMPLETED'
                    """,
                ROW_MAPPER,
                readerId
        );
    }

    @Override
    public List<Order> getAllNonCompleteWriterOrders(long writerId) {
        return jdbcTemplate.query(
                """
                    SELECT o.*, b.*, w.*, r.email AS reader_email
                    FROM orders o
                    JOIN users w ON o.writer_id = w.user_id
                    JOIN users r ON o.buyer_id = r.user_id
                    JOIN books b ON o.book_id = b.book_id
                    WHERE o.writer_id = ? AND status <> 'COMPLETED'
                    """,
                ROW_MAPPER,
                writerId
        );
    }
}


