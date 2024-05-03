package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BuyController {

    private final OrderService os;

    @Autowired
    public BuyController(final OrderService os){
        this.os = os;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo")
    public ModelAndView sendBuyInfo(@RequestParam("bookId") long bookId){

        os.create(bookId);
        //ms.sendEmail(us.getLoggedUser().orElseThrow(UserNotFoundException::new).getEmail(), bookTitle);
        return new ModelAndView("orderSummary");
    }

    @ExceptionHandler({Exception.class})
    public ModelAndView handleException(Exception ex){
        return new ModelAndView("errors/403"); //TODO: dedicated error page
    }


}
