package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.OrderService;
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
}
