package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.QuestionDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.QuestionService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.InvalidPageException;
import ar.edu.itba.paw.models.exception.QuestionNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.questions.Question;
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
    public PaginatedContent<Question> getAll(long bookId, int pageNumber, int pageSize, boolean isAuthor) {
        if (pageNumber < 1) {
            throw new InvalidPageException();
        }

        List<Question> questions;
        long size;

        if (isAuthor) {
            questions = questionDao.getAll(bookId, (pageNumber-1)*pageSize, pageSize);
            size = questionDao.getAllSize(bookId);
        } else if (us.isLoggedIn()){
            questions =  questionDao.getAllFullQuestionsNotUser(bookId, us.getLoggedUser().get().getUserId(), (pageNumber-1)*pageSize, pageSize);
            size = questionDao.getAllFullQuestionsNotUsersSize(bookId, us.getLoggedUser().get().getUserId());
        } else {
            questions = questionDao.getAllFullQuestions(bookId, (pageNumber-1)*pageSize, pageSize);
            size = questionDao.getAllFullQuestionsSize(bookId);
        }


        PaginatedContent<Question> page = new PaginatedContent<>(questions, pageNumber, pageSize, size);

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAll(bookId, page.getPageCount(), pageSize, isAuthor);
        } else {
            return page;
        }
    }

    @Transactional
    @Override
    public PaginatedContent<Question> getAllFromUser(long userId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }


        long size = questionDao.getAllFromUserSize(userId);

        List<Question> questions = questionDao.getAllFromUser(userId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Question> page = new PaginatedContent<>(questions, pageNumber, pageSize, size);

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAllFromUser(userId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Transactional
    @Override
    public PaginatedContent<Question> getAllFromUserAndBook(long userId, long bookId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }


        long size = questionDao.getAllFromUserAndBookSize(userId, bookId);

        List<Question> questions = questionDao.getAllFromUserAndBook(userId, bookId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Question> page = new PaginatedContent<>(questions, pageNumber, pageSize, size);

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAllFromUserAndBook(userId, bookId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Transactional
    @Override
    public PaginatedContent<Question> getAllFromWriter(long userId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }


        long size = questionDao.getAllFromWriterSize(userId);

        List<Question> questions = questionDao.getAllFromWriter(userId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Question> page = new PaginatedContent<>(questions, pageNumber, pageSize, size);

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAllFromWriter(userId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Transactional
    @Override
    public PaginatedContent<Question> getAllFullQuestionsNotUser(long userId, long bookId, int pageNumber, int pageSize) {
        if (pageNumber < 1){
            throw new InvalidPageException();
        }


        long size = questionDao.getAllFullQuestionsNotUsersSize(bookId, userId);

        List<Question> questions = questionDao.getAllFullQuestionsNotUser(bookId, userId, (pageNumber-1)*pageSize, pageSize);

        PaginatedContent<Question> page = new PaginatedContent<>(questions, pageNumber, pageSize, size);

        if (page.getPage().isEmpty() && page.getPageCount() != 0){
            return getAllFullQuestionsNotUser(bookId, userId, page.getPageCount(), pageSize);
        } else {
            return page;
        }
    }

    @Transactional(readOnly = true)
    @Override
    public long getQuestionCount(long bookId, User user, boolean includeUnanswered) {
        if (user == null) {
            return questionDao.getAllFullQuestionsSize(bookId);
        } else {
            if (includeUnanswered) {
                return questionDao.getAllSize(bookId);
            } else {
                return questionDao.getAllFullQuestionsNotUsersSize(bookId, user.getUserId());
            }
        }
    }

    @Transactional(readOnly = true)
    @Override
    public long getMyQuestionCount(long userId, long bookId) {
        return questionDao.getAllFromUserAndBookSize(userId, bookId);
    }

    @Override
    public boolean canAnswer(long questionId, String email) {
        Question q = questionDao.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        return q.getBook().getWriter().getEmail().equals(email);
    }
}
