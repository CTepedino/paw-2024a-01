package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.InvalidCodeException;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Optional;

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

    public boolean canCreateOrder(String id){
        try {
            long bookId = Long.parseLong(id);
            return us.isLoggedIn() && os.canCreateOrder(bookId);
        } catch (Exception e){
            return false;
        }
    }


    public boolean canAccessReceipt(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }

        long orderId =  Long.parseLong(id);

        Order order = os.findById(orderId).orElseThrow(OrderNotFoundException::new);

        String buyerEmail = order.getBuyer().getEmail();
        String writerEmail = order.getWriter().getEmail();
        String userEmail = auth.getName();

        return userEmail.equals(buyerEmail) || userEmail.equals(writerEmail);
    }

    public boolean canAccessBook(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }

        long bookId = Long.parseLong(id);
        return os.hasBookFileAccess(bookId, auth.getName());
    }

    public boolean canAdvanceOrder(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }

        long orderId = Long.parseLong(id);
        return os.canAdvanceOrder(orderId, auth.getName());
    }

    public boolean canEditBook(String id){
        if (!us.isLoggedIn()) {
            return false;
        }
        User user = us.getLoggedUser().get();
        long bookId = Long.parseLong(id);
        Optional<Book> maybeBook = bs.findById(bookId);

        return maybeBook.isPresent() &&  bs.isAuthor(bs.findById(bookId).get(), user.getUserId());
    }

    public boolean canReview(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }
        long bookId = Long.parseLong(id);
        return os.hasBookFileAccess(bookId, auth.getName()) && !bs.isAuthor(bookId, auth.getName());
    }


    public boolean canQuestion(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }
        long bookId = Long.parseLong(id);
        return !bs.isAuthor(bookId, auth.getName());
    }

    public boolean canAnswer(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }
        long questionId = Long.parseLong(id);
        return qs.canAnswer(questionId, auth.getName());
    }


    public boolean checkIsWriter(String id){
        long userId = Long.parseLong(id);
        User user = us.findById(userId).orElseThrow(UserNotFoundException::new);
        return user.getRoles().contains(UserRoles.WRITER);
    }

    public boolean canRecommendBook(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }

        long bookId = Long.parseLong(id);
        return os.hasBookFileAccess(bookId, auth.getName()) && !bs.isAuthor(bookId, auth.getName());
    }

    public boolean checkIsLoggedUser(String id){
        if (!us.isLoggedIn()) {
            return false;
        }
        long userId = Long.parseLong(id);
        return us.getLoggedUser().get().getUserId() == userId;
    }

    public boolean validResetCode(String id, String code){
        if (us.isLoggedIn()) {
            return false;
        }
        long userId = Long.parseLong(id);
        Optional<User> maybeUser = us.findById(userId);
        if (maybeUser.isEmpty()) {
            return false;
        }
        User user = maybeUser.get();
        if (user.getResetCode() == null || !user.getResetCode().getCode().equals(code)) {
            return false;
        }
        return true;
    }
}
