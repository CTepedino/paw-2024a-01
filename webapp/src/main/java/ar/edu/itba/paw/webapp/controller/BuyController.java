package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BuyController {

    private final MailService ms;

    @Autowired
    public BuyController(final MailService ms){
        this.ms = ms;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/buy")
    public ModelAndView buyForm(@RequestParam("writerEmail") String writerEmail, @RequestParam("bookTitle") String bookTitle){
        ModelAndView mav = new ModelAndView("buyForm");
        mav.addObject("writerEmail", writerEmail);
        mav.addObject("bookTitle", bookTitle);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo")
    public ModelAndView sendBuyInfo(@RequestParam("name") String name, @RequestParam("lastName") String lastName, @RequestParam("email") String email, @RequestParam("writerEmail") String writerEmail, @RequestParam("bookTitle") String bookTitle){
        ms.sendEmail(writerEmail, name, lastName, email, bookTitle);
        return new ModelAndView("orderSummary");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cardBuy")
    public ModelAndView cardbuy(){
        return new ModelAndView("salesView");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/purchasesInfo")
    public ModelAndView purchasesInfo(){
        return new ModelAndView("purchasesView");
    }

}
