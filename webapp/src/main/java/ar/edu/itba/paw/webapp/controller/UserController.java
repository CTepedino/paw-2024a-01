package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController {

    private final UserService us;

    @Autowired
    public UserController(UserService us) {
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/login")
    public ModelAndView login(){
        return new ModelAndView("login");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/register")
    public ModelAndView register(@ModelAttribute("registerForm") final UserForm form){
        return new ModelAndView("register");
    }

    @RequestMapping(method = RequestMethod.POST, path = "/register")
    public ModelAndView register(@Valid @ModelAttribute("registerForm") final UserForm form, BindingResult errors){
        if(errors.hasErrors()){
            return register(form);
        }

        // TODO: Pasar datos a UserService
        return new ModelAndView("redirect:/login");
    }

}
