package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.OrderDao;
import ar.edu.itba.paw.models.Order;
import ar.edu.itba.paw.models.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class OrderJdbcDao implements OrderDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public OrderJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .usingGeneratedKeyColumns("order_id")
                .withTableName("orders");
    }

    @Override
    public Optional<Order> findById(long id) {
        final List<Order> list = new ArrayList<>();
        //TODO
        return list.stream().findFirst();
    }

    @Override
    public Order create(long bookId, long buyerId, PaymentMethod paymentMethod, LocalDate date) {
        //TODO
        return new Order(1, bookId, buyerId, paymentMethod, date);
    }
}


