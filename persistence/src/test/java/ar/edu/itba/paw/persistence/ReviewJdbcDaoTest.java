package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.function.Predicate;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewJdbcDaoTest {

    @Autowired
    private DataSource ds;

    @Autowired
    private ReviewDao reviewDao;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate(){
        reviewDao.create(1, 1, 10, "");

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "reviewer_id = 1 AND book_id = 1"));
    }

    @Test
    public void testCreateInvalidRating(){
        assertThrows(
                DataIntegrityViolationException.class,
                () -> reviewDao.create(1, 1, 10000, "")
        );

        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "reviewer_id = 1 AND book_id = 1"));
    }

    @Test
    public void testUpdate(){
        reviewDao.modify(1, 3, 9, "very good");

        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "reviewer_id = 3 AND book_id = 1 AND rating = 9 AND review = 'very good'"));
    }

    @Test
    public void testGetAllExcept(){
        List<Review> reviews =  reviewDao.getAllExcept(1, ReviewOrderBy.DATE_DESC, 0, 100, 3);

        assertNotNull(reviews);
        assertFalse(reviews.stream().anyMatch((review) -> review.getReviewer().getUserId() == 3));
        assertEquals(JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "reviews", "reviewer_id <> 3"), reviews.size());
    }

    @Test
    public void testGetAverageRating(){
        jdbcTemplate.execute("INSERT INTO reviews (reviewer_id, book_id, rating, review) VALUES (1, 2, 2, ''), (2, 2, 10, '')");

        int avg = reviewDao.getAverageRating(2);

        assertEquals(6, avg);
    }

    @Test
    public void testGetAverageRatingNoReviews(){

        int avg = reviewDao.getAverageRating(3);

        assertEquals(0, avg);
    }
}
