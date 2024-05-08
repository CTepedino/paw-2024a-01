package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class SessionController {
    private final UserService us;
    @Autowired
    public SessionController(UserService us){
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path="/signup")
    public ModelAndView signupForm(@ModelAttribute("signUpForm") final SignUpForm form){
        return new ModelAndView("register");
    }

    @RequestMapping(method = RequestMethod.POST, path="/signup")
    public ModelAndView sigunUp(@Valid @ModelAttribute("signUpForm") final SignUpForm form, final BindingResult errors){

        if (errors.hasErrors()){
            form.setPassword(null);
            form.setRepeatPassword(null);
            return signupForm(form);
        }

        us.create(
            form.getEmail(),
            form.getPassword(),
            form.getFirstName(),
            form.getLastName()
        );

        return new ModelAndView("registerConfirmation");
    }

    @RequestMapping(method = RequestMethod.GET, path="/validate")
    public ModelAndView validate(@RequestParam("email") String email, @RequestParam("code") String code){
        us.validateEmail(email, code);
        return new ModelAndView("validationSuccess");
    }

    @RequestMapping(method = RequestMethod.GET, path="/login")
    public ModelAndView loginForm(@RequestParam(name = "error", required = false) String error){
        ModelAndView mav =  new ModelAndView("login");
        mav.addObject("error", error);
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="/changePassword")
    public ModelAndView changePasswordForm(@ModelAttribute("passwordForm") ChangePasswordForm form){
        return new ModelAndView("changePassword");
    }

    @RequestMapping(method = RequestMethod.POST, path="/changePassword")
    public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") ChangePasswordForm form, final BindingResult errors){
        if (errors.hasErrors()){
            return changePasswordForm(form);
        }
        us.changePassword(form.getPassword());
        return new ModelAndView("redirect:/profile");
    }

}

