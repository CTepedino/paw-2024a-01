package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
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

    @Autowired
    public AccessHelper(OrderService os, BookService bs, UserService us){
        this.os = os;
        this.bs = bs;
        this.us = us;
    }

    public boolean canCreateOrder(Authentication auth, String id){
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

    public boolean canEditBook(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }

        long bookId = Long.parseLong(id);
        return bs.loggedUserIsAuthor(bookId);
    }

    public boolean canReview(Authentication auth, String id){
        if (!us.isLoggedIn()) {
            return false;
        }

        long bookId = Long.parseLong(id);
        return os.hasBookFileAccess(bookId, auth.getName());
    }

    public boolean checkIsWriter(String id){
        long userId = Long.parseLong(id);
        return us.getRoles(userId).contains(UserRoles.WRITER);
    }
}
