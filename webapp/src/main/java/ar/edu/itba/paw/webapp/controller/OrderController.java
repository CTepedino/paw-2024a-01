package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderOrderBy;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.OrderSearchForm;
import ar.edu.itba.paw.webapp.form.UpdateOrderForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    public ModelAndView purchases(
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("updateOrderForm") UpdateOrderForm updateOrderForm,
            @Valid @ModelAttribute("orderSearchForm") final OrderSearchForm orderSearchForm,
            final BindingResult error
    ){
        if(error.hasErrors()){
            return new ModelAndView("purchasesView");
        }
        final ModelAndView mav = new ModelAndView("purchasesView");
        mav.addObject("orders", os.getReaderOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), OrderOrderBy.DATE_DESC, orderSearchForm.getPage(), 10));
        mav.addObject("statuses", OrderStatus.values());
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/sales")
    public ModelAndView sales(
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("updateOrderForm") UpdateOrderForm updateOrderForm,
            @Valid @ModelAttribute("orderSearchForm") final OrderSearchForm form,
            final BindingResult error)
    {
        if(error.hasErrors()){
            return new ModelAndView("salesView");
        }
        ModelAndView mav = new ModelAndView("salesView");
        mav.addObject("orders", os.getWriterOrders(loggedUser.getUserId(), form.getTitle(), form.getOrderStatus(), OrderOrderBy.DATE_DESC, form.getPage(), 10));
        mav.addObject("statuses", OrderStatus.values());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path="/advanceOrder/{id:\\d+}/{from:sales|purchases}")
    public ModelAndView advanceOrder(@ModelAttribute("updateOrderForm") UpdateOrderForm form, @PathVariable long id, @PathVariable String from){
        os.updateOrder(id, form.getReceipt(), form.getApproved());
        return new ModelAndView("redirect:/" + from);
    }




    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo")
    public ModelAndView sendBuyInfo(@ModelAttribute("loggedUser") User loggedUser, @RequestParam("bookId") long bookId){

        os.create(bookId, null);

        ModelAndView mav = new ModelAndView("orderSummary");
        Order order = os.find(loggedUser.getUserId(), bookId).orElseThrow(OrderNotFoundException::new);
        mav.addObject("order", order);
        return new ModelAndView("orderSummary");
    }


}

