package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.MailService;
import ar.edu.itba.paw.interfaces.WriterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BuyController {

    private final MailService ms;
    private final WriterService ws;

    @Autowired
    public BuyController(final MailService ms, final WriterService ws){
        this.ms = ms;
        this.ws = ws;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/buy")
    public ModelAndView buyForm(@RequestParam("writerId") Long writerId){
        ModelAndView mav = new ModelAndView("buyForm");
        mav.addObject("writerId", writerId);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/sendBuyInfo")
    public ModelAndView sendBuyInfo(@RequestParam("name") String name, @RequestParam("lastName") String lastName, @RequestParam("email") String email, @RequestParam("writerId") Long writerId){
        ms.sendEmail(ws.findById(writerId).get().getEmail(), name, lastName, email);
        return new ModelAndView("emailConfirmation");
    }

}
