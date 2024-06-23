package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.users.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.junit.Assert.assertEquals;


@Transactional
@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class QuestionJpaDaoTest {

    private static final User TEST_USER = new User(101, "", "", "", "", false, Locale.US);

    private static final Book TEST_BOOK = new Book(101, "", "", BookGenre.BIOGRAPHY, BigDecimal.ONE, 1, 1, LocalDate.now(), TEST_USER, false);

    @Autowired
    private QuestionJpaDao questionDao;

    @PersistenceContext
    private EntityManager em;

    @Test
    public void testCreate(){
        Question question = questionDao.create(TEST_BOOK, TEST_USER, "is it a book?", LocalDateTime.now());

        Assert.assertNotNull(question);
        Assert.assertEquals(TEST_BOOK, question.getBook());
        Assert.assertEquals(TEST_USER, question.getQuestioner());
        Assert.assertEquals("is it a book?", question.getQuestion());
        Assert.assertEquals(1, TestUtils.getRowCount(em, "FROM questions q WHERE q.book_id = 101 AND q.questioner_id = 101 AND q.question = 'is it a book?'"));
    }

    @Test
    public void testFindByIdExisting(){
        Optional<Question> maybeQuestion = questionDao.findById(101);

        Assert.assertNotNull(maybeQuestion);
        Assert.assertTrue(maybeQuestion.isPresent());
    }

    @Test
    public void testFindByIdNonExisting(){
        Optional<Question> maybeQuestion = questionDao.findById(999);

        Assert.assertNotNull(maybeQuestion);
        Assert.assertTrue(maybeQuestion.isEmpty());
    }

    @Test
    public void testAnswer(){
        Question question = em.find(Question.class, 101L);

        questionDao.answer(question, "yes", LocalDateTime.now());

        Assert.assertEquals("yes", question.getAnswer());
        Assert.assertEquals(1, TestUtils.getRowCount(em, "FROM questions q WHERE q.book_id = 101 AND q.answer = 'yes'"));
    }

    @Test
    public void testGetAllFromUserAndBook(){

        List<Question> questions = questionDao.getAllFromUserAndBook(101, 101, 0, 999);

        Assert.assertNotNull(questions);
        Assert.assertEquals(TestUtils.getRowCount(em, "FROM questions WHERE questioner_id = 101 AND book_id = 101") ,questions.size());
    }

    @Test
    public void testGetAllFromWriter(){

        List<Question> questions = questionDao.getAllFromWriter(101, 0, 999);

        Assert.assertNotNull(questions);
        Assert.assertEquals(TestUtils.getRowCount(em, "FROM questions q LEFT JOIN books b ON q.book_id = b.book_id WHERE b.writer_id = 101"), questions.size());
    }

    @Test
    public void testGetAllFullQuestionsNotUser(){
        em.createNativeQuery("INSERT INTO questions (question_id, answer, answer_date, date, question, book_id, questioner_id) VALUES (1, '', NOW(), NOW(), '', 102, 102), (2, NULL, NULL, NOW(), '', 102, 101), (3, '', NOW(), NOW(), '', 102, 103)").executeUpdate();

        List<Question> questions = questionDao.getAllFullQuestionsNotUser(102, 102, 0, 999);
        
        Assert.assertNotNull(questions);
        Assert.assertTrue(questions.stream().filter(q -> q.getQuestioner().getUserId() == 102 || q.getAnswer() == null).findAny().isEmpty());
        Assert.assertEquals(TestUtils.getRowCount(em,"FROM questions WHERE questioner_id <> 102 AND answer IS NOT NULL"), questions.size());
    }
}
