package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.OrderService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class OrderController {

    private final OrderService os;
    private final UserService us;
    private final BookService bs;



    @Autowired
    public OrderController(final OrderService os, final UserService us, final BookService bs){
        this.os = os;
        this.us = us;
        this.bs = bs;
    }



    @RequestMapping(method = RequestMethod.GET, path="/purchases")
    public ModelAndView purchases(){
        ModelAndView mav = new ModelAndView("purchasesView");
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        mav.addObject("orders", os.getAllReaderOrders(user.getUserId()));
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/sales")
    public ModelAndView sales(){
        ModelAndView mav = new ModelAndView("salesView");
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        mav.addObject("orders", os.getAllWriterOrders(user.getUserId()));
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }

}

