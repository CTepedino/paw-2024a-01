package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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
        return new ModelAndView("test/signup");
    }

    @RequestMapping(method = RequestMethod.POST, path="/signup")
    public ModelAndView sigunUp(@Valid @ModelAttribute("signUpForm") final SignUpForm form, final BindingResult errors){

        if (errors.hasErrors()){
            return signupForm(form);
        }

        us.create(
                form.getFirstName(),
                form.getLastName(),
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
}

/*
    final CybraryAuthUserDetails userDetails = (CybraryAuthUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
*/