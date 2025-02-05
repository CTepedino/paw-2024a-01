package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.dao.ReviewDao;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.*;

@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class ReviewJpaDaoTest {

    private static final User REVIEWER = new User(101, "", "", "", "", false, Locale.US);

    @Autowired
    private ReviewDao reviewDao;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testCreate(){
        reviewDao.create(101, REVIEWER, 10, "", LocalDateTime.now());

        assertEquals(1, TestUtils.getRowCount(em, "FROM reviews WHERE reviewer_id = 101 AND book_id = 101"));
    }

    @Test
    public void testUpdate(){
        Review review = em.find(Review.class, 101L);

        reviewDao.modify(review, 9, "very good", LocalDateTime.now());

        assertEquals(1, TestUtils.getRowCount(em, "FROM reviews WHERE reviewer_id = 103 AND book_id = 102 AND rating = 9 AND review = 'very good'"));
    }

    @Test
    public void testGetAllExcept(){
        List<Review> reviews =  reviewDao.getAllExcept(101, ReviewOrderBy.DATE_DESC, 0, 100, 103);

        assertNotNull(reviews);
        assertFalse(reviews.stream().anyMatch((review) -> review.getReviewer().getUserId() == 103));
        assertEquals(TestUtils.getRowCount(em, "FROM reviews WHERE reviewer_id <> 103"), reviews.size());
    }

}