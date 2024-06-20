package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.MailService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
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
    public ModelAndView validate(@RequestParam("id") long id, @RequestParam("code") String code){
        us.validateEmail(id, code);
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
    public ModelAndView changePassword(
            @Valid @ModelAttribute("passwordForm") ChangePasswordForm form,
            final BindingResult errors,
            @ModelAttribute("loggedUser") User loggedUser
    ){
        if (errors.hasErrors()){
            return changePasswordForm(form);
        }

        us.changePassword(form.getPassword());
        return new ModelAndView("redirect:/profile/"+loggedUser.getUserId());
    }


    @RequestMapping(method = RequestMethod.GET, path="/forgotPassword")
    public ModelAndView forgotPassword(@ModelAttribute("sendForgotPasswordEmailForm") ForgotPasswordEmailForm form){
        return new ModelAndView("forgotPassword");
    }

    @RequestMapping(method = RequestMethod.POST, path="/forgotPassword")
    public ModelAndView sendForgotPasswordMail(@Valid @ModelAttribute("sendForgotPasswordEmailForm") ForgotPasswordEmailForm form, final BindingResult errors){
        if(errors.hasErrors()){
            return forgotPassword(form);
        }

        us.createResetPasswordCode(form.getEmail());
        ModelAndView mav = new ModelAndView("sentForgotPasswordMail");
        mav.addObject("id", us.findByEmail(form.getEmail()).get().getUserId());
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/resetPassword/{id:\\d+}/{code:\\d+}")
    public ModelAndView resetPasswordForm(@ModelAttribute("resetPasswordForm") ResetPasswordForm form, @PathVariable("id") long id, @PathVariable("code") String code){

        ModelAndView mav =  new ModelAndView("resetPasswordForm");
        mav.addObject("id",id);
        mav.addObject("code", code);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path="/resetPassword/{id:\\d+}/{code:\\d+}")
    public ModelAndView resetPassword(@Valid @ModelAttribute("resetPasswordForm") ResetPasswordForm form, final BindingResult errors, @PathVariable("id") long id, @PathVariable("code") String code){
        if (errors.hasErrors()){
            resetPasswordForm(form, id, code);
        }

        us.resetPassword(id, form.getPassword(), code);
        return new ModelAndView("resetPasswordSuccess");
    }

    @RequestMapping(method = RequestMethod.POST, path="/resendResetCode/{userId:\\d+}")
    public ModelAndView resendResetCode(@PathVariable("userId") long userId){
        us.resendResetCode(userId);
        ModelAndView mav = new ModelAndView("sentForgotPasswordMail");
        mav.addObject("id", us.findById(userId).get().getUserId());
        return mav;
    }
}

