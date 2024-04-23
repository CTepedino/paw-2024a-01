package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.auth.CybraryAuthUserDetails;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.form.WriterNameForm;
import ar.edu.itba.paw.webapp.util.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

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
            return signupForm(form);
        }

        us.create(
                form.getEmail(),
                form.getPassword()
        );

        return new ModelAndView("registerConfirmation");
    }

    @RequestMapping(method = RequestMethod.GET, path="/login")
    public ModelAndView loginForm(){
        return new ModelAndView("login");
    }


    @RequestMapping(method = RequestMethod.GET, path="/profile")
    public ModelAndView profile(Authentication authentication){
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.findByEmail(authentication.getName()).get());
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }


    @RequestMapping(method = RequestMethod.POST, path="/signup/writer")
    public ModelAndView registerAsWriter(@Valid @ModelAttribute("writerNameForm")WriterNameForm form, final BindingResult errors){
        if (errors.hasErrors()){
            return registerAsWriterForm(form);
        }
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        us.giveWriterRole(user.getUserId(), form.getFirstName(), form.getLastName());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET, path="/signup/writer")
    public ModelAndView registerAsWriterForm(@ModelAttribute("writerNameForm") WriterNameForm form){
        return new ModelAndView("nameForm");
    }

    @RequestMapping(method = RequestMethod.GET, path="/changePassword")
    public ModelAndView changePasswordForm(@ModelAttribute("passwordForm") SignUpForm form){
        form.setEmail(us.getLoggedUser().get().getEmail());
        return new ModelAndView("changePassword");
    }

    @RequestMapping(method = RequestMethod.POST, path="/changePassword")
    public ModelAndView changePassword(@Valid @ModelAttribute("passwordForm") SignUpForm form, final BindingResult errors){
        if (errors.hasErrors()){
            return changePasswordForm(form);
        }
        us.fillMissingWriterData(us.getLoggedUser().get().getUserId(), form.getPassword());
        return new ModelAndView("redirect:/profile");
    }

    @RequestMapping(method = RequestMethod.GET, path="/purchases")
    public ModelAndView purchases(){
        ModelAndView mav = new ModelAndView("purchasesView");
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/sales")
    public ModelAndView sales(){
        ModelAndView mav = new ModelAndView("salesView");
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }

}

