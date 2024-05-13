package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.exception.OrderNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CreateOrderForm;
import ar.edu.itba.paw.webapp.form.NewBookForm;
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

    private static final int ORDER_PAGE_SIZE = 10;

    private final OrderService os;
    private final UserService us;
    private final BookService bs;



    @Autowired
    public OrderController(final OrderService os, final UserService us, final BookService bs){
        this.os = os;
        this.us = us;
        this.bs = bs;
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
            if (error.hasFieldErrors("page")){
                orderSearchForm.setPage(1);
            }
            if (error.hasFieldErrors("orderStatus")){
                orderSearchForm.setOrderStatus(null);
            }
        }
        final ModelAndView mav = new ModelAndView("purchasesView");
        mav.addObject("orders", os.getReaderOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), orderSearchForm.getPage(), ORDER_PAGE_SIZE));
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
            if (error.hasFieldErrors("page")){
                orderSearchForm.setPage(1);
            }
            if (error.hasFieldErrors("orderStatus")){
                orderSearchForm.setOrderStatus(null);
            }
        }
        ModelAndView mav = new ModelAndView("salesView");
        mav.addObject("orders", os.getWriterOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), orderSearchForm.getPage(), ORDER_PAGE_SIZE));
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



    @RequestMapping(method = RequestMethod.GET, path = "/sendBuyInfo/{id:\\d+}")
    public ModelAndView sendBuyInfoForm(@ModelAttribute("createOrderForm") CreateOrderForm form, @PathVariable long id){

        Book book = bs.findById(id).orElseThrow(BookNotFoundException::new);

        ModelAndView mav =  new ModelAndView("sendReceiptForm");
        mav.addObject("book", book);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo/{id:\\d+}")
    public ModelAndView sendBuyInfo(@Valid @ModelAttribute final CreateOrderForm form, final BindingResult errors, @PathVariable("id") long bookId){

        if(errors.hasErrors()){
            return sendBuyInfoForm(form, bookId);
        }

        os.create(bookId, form.getReceipt());

        ModelAndView mav = new ModelAndView("orderSummary");
        Order order = os.find(us.getLoggedUser().orElseThrow(UserNotFoundException::new).getUserId(), bookId).orElseThrow(OrderNotFoundException::new);
        mav.addObject("order", order);
        return mav;
    }


}

