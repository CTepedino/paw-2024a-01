package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class OrderController {

    private static final int ORDER_PAGE_SIZE = 10;

    private final OrderService os;
    private final BookService bs;



    @Autowired
    public OrderController(final OrderService os, final BookService bs){
        this.os = os;
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

        PaginatedContent<Order> orders = os.getReaderOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), orderSearchForm.getPage(), ORDER_PAGE_SIZE);

        final ModelAndView mav = new ModelAndView("purchasesView");
        mav.addObject("orders", orders);
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

        PaginatedContent<Order> orders = os.getWriterOrders(loggedUser.getUserId(), orderSearchForm.getTitle(), orderSearchForm.getOrderStatus(), orderSearchForm.getPage(), ORDER_PAGE_SIZE);

        ModelAndView mav = new ModelAndView("salesView");
        mav.addObject("orders", orders);
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

    @RequestMapping(method = RequestMethod.POST, path="/recommendBook/{id:\\d+}/purchases")
    public ModelAndView recommendBook(
            @RequestParam(name = "recommended", required = false, defaultValue = "false") boolean recommended,
            @PathVariable long id
    ){
        os.recommendBook(id, recommended);
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
    public ModelAndView sendBuyInfo(@Valid @ModelAttribute final CreateOrderForm form, final BindingResult errors, @PathVariable("id") long bookId, @ModelAttribute("loggedUser") User loggedUser){

        if(errors.hasErrors()){
            return sendBuyInfoForm(form, bookId);
        }

        Order order = os.create(bookId, form.getReceipt());

        ModelAndView mav = new ModelAndView("orderSummary");
        mav.addObject("order", order);
        return mav;
    }


}

