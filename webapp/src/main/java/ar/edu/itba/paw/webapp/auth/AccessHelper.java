package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.orders.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class AccessHelper {

    private final OrderService os;

    @Autowired
    public AccessHelper(OrderService os){
        this.os = os;
    }

    public boolean canCreateOrder(Authentication auth, HttpServletRequest request){
        try {
            long bookId = Long.parseLong(request.getParameter("bookId"));
            return auth.isAuthenticated() && os.canCreateOrder(bookId);
        } catch (NumberFormatException e){
            return false;
        }
    }

    public boolean canAccessReceipt(Authentication auth, String id){

        long orderId =  Long.parseLong(id);
        Order order = os.findById(orderId).orElseThrow(OrderNotFoundException::new);

        String buyerEmail = order.getBuyer().getEmail();
        String writerEmail = order.getWriter().getEmail();
        String userEmail = auth.getName();

        return userEmail.equals(buyerEmail) || userEmail.equals(writerEmail);
    }
}
