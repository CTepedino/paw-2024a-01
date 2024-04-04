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
    public ModelAndView buyForm(){
        return new ModelAndView("buyForm");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo")
    public ModelAndView sendBuyInfo(@RequestParam("name") String name, @RequestParam("lastName") String lastName, @RequestParam("email") String email){
        ms.sendEmail("mivaw93421@evimzo.com", name, lastName, email);
        return new ModelAndView("emailConfirmation");
    }

}
