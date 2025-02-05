package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.QuestionDao;
import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.*;
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

    private final OrderService os;

    private final MailService ms;


    private final static Logger LOGGER = LoggerFactory.getLogger(QuestionServiceImpl.class);


    public QuestionServiceImpl(QuestionDao questionDao, BookService bs, UserService us, OrderService os, MailService ms) {
        this.questionDao = questionDao;
        this.bs = bs;
        this.us = us;
        this.os = os;
        this.ms = ms;
    }

    @Transactional
    @Override
    public long create(long bookId, String question) {
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        User questioner = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        if (book.getWriter().getUserId() == questioner.getUserId() || os.existsOrder(questioner.getUserId(), bookId)){
            throw new IllegalQuestionException();
        }
        Question q = questionDao.create(book, questioner, question, LocalDateTime.now());
        ms.sendQuestionReceivedEmail(q);
        LOGGER.atDebug().setMessage("Created Question for bookId: {}").addArgument(bookId).log();

        return q.getQuestionId();
    }

    @Transactional
    @Override
    public void answer(long questionId, String answer) {
        Question question = questionDao.findById(questionId).orElseThrow(QuestionNotFoundException::new);
        questionDao.answer(question, answer, LocalDateTime.now());
        ms.sendAnswerReceivedEmail(question);
        LOGGER.atDebug().setMessage("Answered Question with questionId: {}").addArgument(questionId).log();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Question> findById(long questionId) {
        return questionDao.findById(questionId);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Question> searchQuestions(Long bookId, Long writerId, Long questionerId, boolean excludeQuestioner, Boolean isAnswered, int pageNumber, int pageSize) {
        List<Question> questions = questionDao.getAll(bookId, writerId, questionerId, excludeQuestioner, isAnswered, (pageNumber-1)*pageSize, pageSize);

        return new PaginatedContent<>(questions, pageNumber, pageSize, questionDao.getAllSize(bookId, writerId, questionerId, excludeQuestioner, isAnswered));
    }
}
