package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.OrderSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class OrderController {

    private final OrderService os;
    private final UserService us;



    @Autowired
    public OrderController(final OrderService os, final UserService us){
        this.os = os;
        this.us = us;
    }



    @RequestMapping(method = RequestMethod.GET, path="/purchases")
    public ModelAndView purchases(@Valid @ModelAttribute("orderSearchForm") final OrderSearchForm form, final BindingResult error){

        if(error.hasErrors()){
            return new ModelAndView("purchasesView");
        }

        final ModelAndView mav = new ModelAndView("purchasesView");

        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);

        mav.addObject("orders", os.getReaderOrders(user.getUserId(), form.getTitle(), form.getOrderStatus(), null, form.getPage(), 10));
        mav.addObject("orderSearchForm", form);
        mav.addObject("statuses", OrderStatus.values());
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/sales")
    public ModelAndView sales(@Valid @ModelAttribute("orderSearchForm") final OrderSearchForm form, final BindingResult error){

        if(error.hasErrors()){
            return new ModelAndView("salesView");
        }

        ModelAndView mav = new ModelAndView("salesView");
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        mav.addObject("orders", os.getReaderOrders(user.getUserId(), form.getTitle(), form.getOrderStatus(), null, form.getPage(), 10));
        mav.addObject("orderSearchForm", form);
        mav.addObject("statuses", OrderStatus.values());
        return mav;
    }


    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo")
    public ModelAndView sendBuyInfo(@RequestParam("bookId") long bookId){

        //os.create(bookId, null);
        //ms.sendEmail(us.getLoggedUser().orElseThrow(UserNotFoundException::new).getEmail(), bookTitle);
        return new ModelAndView("orderSummary");
    }

    @RequestMapping(method = RequestMethod.POST, path="/advanceOrder")
    public ModelAndView advanceOrder(@RequestParam("bookId") long bookId, @RequestParam("writerId") long writerId, @RequestParam("buyerId") long buyerId, @RequestParam("from") String from){
        Order order = os.find(buyerId, bookId).orElseThrow(OrderNotFoundException::new);
        os.toNextStatus(order);
        return new ModelAndView("redirect:/"+from);
    }


}

