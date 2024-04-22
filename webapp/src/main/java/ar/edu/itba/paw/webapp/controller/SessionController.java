package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.auth.CybraryAuthUserDetails;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import ar.edu.itba.paw.webapp.form.WriterNameForm;
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

@Controller
public class SessionController {
    private final UserService us;

    @Autowired
    public SessionController(UserService us){
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path="/signup")
    public ModelAndView signupForm(@ModelAttribute("signUpForm") final SignUpForm form){
        return new ModelAndView("test/signup");
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

        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET, path="/login")
    public ModelAndView loginForm(){
        return new ModelAndView("test/login");
    }


    @RequestMapping(method = RequestMethod.GET, path="/profile")
    public ModelAndView profile(Authentication authentication){
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.findByEmail(authentication.getName()));
        return mav;
    }

    @ModelAttribute("user")
    public User loggedUser(final HttpSession session){
        final CybraryAuthUserDetails userDetails = (CybraryAuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (userDetails == null){
            return null;
        }
        return us.findByEmail(userDetails.getUsername()).orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(method = RequestMethod.POST, path="/signup/writer")
    public ModelAndView registerAsWriter( @ModelAttribute("user") User user, @Valid @ModelAttribute("writerNameForm")WriterNameForm form, final BindingResult errors){
        if (errors.hasErrors()){
            return registerAsWriterForm(form);
        }
        us.giveWriterRole(user.getUserId(), form.getFirstName(), form.getLastName());
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(method = RequestMethod.GET, path="/signup/writer")
    public ModelAndView registerAsWriterForm(@ModelAttribute("writerNameForm") WriterNameForm form){
        return new ModelAndView("/test/nameForm");
    }
}

