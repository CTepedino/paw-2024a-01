package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReviewJdbcDao implements ReviewDao {

    private static final RowMapper<Review> ROW_MAPPER = (rs, rowNum) -> new Review(
            rs.getLong("book_id"),
            UserJdbcDao.USER_ROW_MAPPER.mapRow(rs, rowNum),
            rs.getInt("rating"),
            rs.getString("review"),
            rs.getTimestamp("date").toLocalDateTime()
    );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public ReviewJdbcDao(final DataSource ds){
        jdbcTemplate = new JdbcTemplate(ds);
        simpleJdbcInsert = new SimpleJdbcInsert(ds)
                .withTableName("reviews")
                .usingColumns("reviewer_id", "book_id", "rating", "review");
    }

    @Transactional
    @Override
    public void create(long bookId, long reviewerId, int rating, String review) {
        Map<String, Object> reviewData = new HashMap<>();
        reviewData.put("book_id", bookId);
        reviewData.put("reviewer_id", reviewerId);
        reviewData.put("rating", rating);
        reviewData.put("review", review);
        simpleJdbcInsert.execute(reviewData);
    }

    @Transactional
    @Override
    public void modify(long bookId, long reviewerId, int rating, String review) {
        jdbcTemplate.update(
        """
                UPDATE reviews
                SET rating = ?, review = ?, date = now()
                WHERE reviewer_id = ? AND book_id = ?
            """,
            rating,
            review,
            reviewerId,
            bookId
        );
    }

    @Transactional
    @Override
    public void delete(long bookId, long reviewerId) {
        jdbcTemplate.update(
            """
                DELETE FROM reviews
                WHERE book_id = ? AND reviewer_id = ?
            """,
            bookId,
            reviewerId
        );
    }

    @Transactional(readOnly = true)
    @Override
    public List<Review> getAll(long bookId, ReviewOrderBy orderBy, int offset, int limit) {
        return jdbcTemplate.query(
        """
                SELECT *
                FROM reviews r JOIN users u ON r.reviewer_id = u.user_id
                WHERE r.book_id = ?
                ORDER BY ?
                OFFSET ? LIMIT ?
            """,
            ROW_MAPPER,
            bookId,
            orderBy.getColumnName(),
            offset,
            limit
        );
    }

    @Override
    public long getAllSize(long bookId) {
        return DaoUtils.getRowCount(jdbcTemplate, "reviews", "WHERE book_id = ?", bookId);
    }

    @Override
    public Optional<Review> get(long bookId, long reviewerId) {
        List<Review> list = jdbcTemplate.query(
            """
                    SELECT *
                    FROM reviews r JOIN users u ON r.reviewer_id = u.user_id
                    WHERE r.book_id = ? AND r.reviewer_id = ?
                """,
                ROW_MAPPER,
                bookId,
                reviewerId
        );
        return list.stream().findFirst();
    }

    @Override
    public int getAverageRating(long bookId) {
        Integer avg = jdbcTemplate.queryForObject(
            """
                   SELECT AVG(rating)
                   FROM reviews
                   WHERE book_id = ?
                """,
                Integer.class,
                bookId
        );
        return avg!=null?avg:0;
    }
}
