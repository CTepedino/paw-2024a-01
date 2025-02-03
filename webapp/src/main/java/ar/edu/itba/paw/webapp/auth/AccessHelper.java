package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.InvalidCodeException;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.exception.QuestionNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;
import java.util.function.Function;
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
        Order order = os.findById(orderId).orElseThrow(OrderNotFoundException::new);

        return us.getLoggedUser()
                .filter(u -> order.getBuyer().getUserId() == u.getUserId() || order.getWriter().getUserId() == u.getUserId())
                .isPresent();
    }

    public boolean isLoggedAndWriterAndCanAdvanceOrder(String orderIdString){
        long orderId = Long.parseLong(orderIdString);
        Order order = os.findById(orderId).orElseThrow(OrderNotFoundException::new);

        return us.getLoggedUser()
                .filter(u -> order.getWriter().getUserId() == u.getUserId())
                .filter(u -> order.getOrderStatus().canWriterAdvance())
                .isPresent();
    }

    public boolean isLoggedAndBuyerAndCanAdvanceOrder(String orderIdString){
        long orderId = Long.parseLong(orderIdString);
        Order order = os.findById(orderId).orElseThrow(OrderNotFoundException::new);

        return us.getLoggedUser()
                .filter(u -> order.getBuyer().getUserId() == u.getUserId())
                .filter(u -> order.getOrderStatus().canReaderAdvance())
                .isPresent();
    }

    public boolean isLoggedAndCanAnswer(String questionIdString){
        long questionId = Long.parseLong(questionIdString);
        Question question = qs.findById(questionId).orElseThrow(QuestionNotFoundException::new);

        return us.getLoggedUser()
                .filter(u -> question.getBook().getWriter().getUserId() == u.getUserId())
                .isPresent();
    }

    private Long parseOrNull(String s){
        try {
           return Long.parseLong(s);
        } catch (Exception e) {
            return null;
        }
    }

}
