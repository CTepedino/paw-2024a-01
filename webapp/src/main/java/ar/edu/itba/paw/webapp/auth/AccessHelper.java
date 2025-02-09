package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.QuestionService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Predicate;

@Component
public class AccessHelper {

    private final OrderService os;
    private final BookService bs;
    private final UserService us;
    private final QuestionService qs;

    @Autowired
    public AccessHelper(OrderService os, BookService bs, UserService us, QuestionService qs){
        this.os = os;
        this.bs = bs;
        this.us = us;
        this.qs = qs;
    }

    private Predicate<User> isLoggedUser(long userId){
        return u -> u.getUserId() == userId;
    }

    private Predicate<User> isAuthor(long bookId){
        return u -> bs.isAuthor(u.getUserId(), bookId);
    }

    private Predicate<User> isOwner(long bookId){
        return u -> os.ownsBook(u.getUserId(), bookId);
    }

    public boolean isLoggedUser(String userIdString){
        long userId = Long.parseLong(userIdString);

        return us.getLoggedUser()
                .filter(isLoggedUser(userId))
                .isPresent();
    }

    public boolean isLoggedAndCanPublish(){
        return us.getLoggedUser()
                .filter(u -> u.getCbu() != null)
                .isPresent();
    }


    public boolean isLoggedAndWriter(String bookIdString){
        long bookId = Long.parseLong(bookIdString);

        return us.getLoggedUser()
                .filter(isAuthor(bookId))
                .isPresent();
    }

    public boolean isLoggedUserAndOwnsBook(String userIdString, String bookIdString){
        long userId = Long.parseLong(userIdString);
        long bookId = Long.parseLong(bookIdString);

        return us.getLoggedUser()
                .filter(isLoggedUser(userId))
                .filter(isOwner(bookId))
                .isPresent();
    }

    public boolean isLoggedAndOwnsBookOrIsWriter(String bookIdString){
        long bookId = Long.parseLong(bookIdString);

        return us.getLoggedUser()
                .filter(isAuthor(bookId)
                        .or(isOwner(bookId)))
                .isPresent();
    }

    public boolean isLoggedAndWriterOrBuyer(String orderIdString){
        long orderId = Long.parseLong(orderIdString);
        Optional<Order> order = os.findById(orderId);

        return order
                .filter(o ->
                    us.getLoggedUser()
                        .filter(u -> o.getBuyer().getUserId() == u.getUserId() || o.getWriter().getUserId() == u.getUserId())
                        .isPresent())
                .isPresent();

    }

    public boolean isLoggedAndWriterAndCanAdvanceOrder(String orderIdString){
        long orderId = Long.parseLong(orderIdString);
        Optional<Order> order = os.findById(orderId);

        return order
                .filter(o ->
                        us.getLoggedUser()
                            .filter(u -> o.getWriter().getUserId() == u.getUserId())
                            .filter(u -> o.getOrderStatus().canWriterAdvance())
                        .isPresent())
                .isPresent();

    }

    public boolean isLoggedAndBuyerAndCanAdvanceOrder(String orderIdString){
        long orderId = Long.parseLong(orderIdString);
        Optional<Order> order = os.findById(orderId);

        return order
                .filter(o ->
                        us.getLoggedUser()
                            .filter(u -> o.getBuyer().getUserId() == u.getUserId())
                            .filter(u -> o.getOrderStatus().canReaderAdvance())
                        .isPresent())
                .isPresent();

    }

    public boolean isLoggedAndCanAnswer(String questionIdString){
        long questionId = Long.parseLong(questionIdString);
        Optional<Question> question = qs.findById(questionId);

        return question
                .filter(q ->
                    us.getLoggedUser()
                        .filter(u -> q.getBook().getWriter().getUserId() == u.getUserId())
                        .isPresent())
                .isPresent();

    }

}
