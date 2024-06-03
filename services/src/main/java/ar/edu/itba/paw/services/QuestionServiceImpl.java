package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.QuestionDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.QuestionService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionServiceImpl implements QuestionService {

    private final QuestionDao questionDao;

    private final BookService bs;

    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);


    public QuestionServiceImpl(QuestionDao questionDao, BookService bs, UserService us) {
        this.questionDao = questionDao;
        this.bs = bs;
        this.us = us;
    }

    @Transactional
    @Override
    public void create(long bookId, String question) {
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        User questioner = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        questionDao.create(book, questioner, question, LocalDateTime.now());
        LOGGER.atDebug().setMessage("Created Question for bookId: {}").addArgument(bookId).log();
    }

    @Transactional
    @Override
    public void answer(long questionId, String answer) {
        Optional<Question> question = questionDao.findById(questionId);
        question.ifPresent(value -> questionDao.answer(value, answer, LocalDateTime.now()));
        LOGGER.atDebug().setMessage("Answered Question with questionId: {}").addArgument(questionId).log();
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Question> getAll(long bookId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }


        long size = questionDao.getAllSize(bookId);

        List<Question> questions = questionDao.getAll(bookId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Question> page = new PaginatedContent<>(questions, pageNumber, pageSize, size);

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAll(bookId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }
}
