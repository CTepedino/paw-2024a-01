package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.webapp.form.OrderSearchForm;
import ar.edu.itba.paw.webapp.form.UpdateOrderFormBuyerSide;
import ar.edu.itba.paw.webapp.form.UpdateOrderFormWriterSide;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OrderController {

    private final OrderService os;
    private final UserService us;



    @Autowired
    public OrderController(final OrderService os, final UserService us){
        this.os = os;
        this.us = us;
    }

    @ModelAttribute("statuses")
    public List<OrderStatus> statuses() {
        return List.of(OrderStatus.values());
    }

    @RequestMapping(method = RequestMethod.GET, path="/purchases")
    public ModelAndView purchases(
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("updateOrderForm") UpdateOrderFormBuyerSide updateOrderForm,
            @Valid @ModelAttribute("orderSearchForm") OrderSearchForm orderSearchForm,
            final BindingResult error
    ){
        if(error.hasErrors()){
            orderSearchForm.setOrderStatus(null);
        }
        final ModelAndView mav = new ModelAndView("purchasesView");
        mav.addObject("orders", os.getReaderOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), orderSearchForm.getPage(), 10));
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/sales")
    public ModelAndView sales(
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("updateOrderForm") UpdateOrderFormWriterSide updateOrderForm,
            @Valid @ModelAttribute("orderSearchForm") OrderSearchForm orderSearchForm,
            final BindingResult error)
    {
        if(error.hasErrors()){
            orderSearchForm.setOrderStatus(null);
        }
        ModelAndView mav = new ModelAndView("salesView");
        mav.addObject("orders", os.getWriterOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), orderSearchForm.getPage(), 10));
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path="/advanceOrder/{id:\\d+}/purchases")
    public ModelAndView advanceOrderBuyerSide(
            @Valid @ModelAttribute("updateOrderForm") UpdateOrderFormBuyerSide form,
            final BindingResult error,
            @PathVariable long id
    ){
        if (error.hasErrors()) {
            return new ModelAndView("redirect:/purchases");
        }

        os.updateOrderBuyerSide(id, form.getReceipt());
        return new ModelAndView("redirect:/purchases");
    }


    @RequestMapping(method = RequestMethod.POST, path="/advanceOrder/{id:\\d+}/sales")
    public ModelAndView advanceOrderWriterSide(
            @Valid @ModelAttribute("updateOrderForm") UpdateOrderFormWriterSide form,
            final BindingResult error,
            @PathVariable long id
    ){
        if (error.hasErrors()) {
            return new ModelAndView("redirect:/sales");
        }

        os.updateOrderWriterSide(id, form.getApproved());
        return new ModelAndView("redirect:/sales");
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

