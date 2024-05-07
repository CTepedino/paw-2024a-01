package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.users.User;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SessionAdvice {

    private final UserService us;

    public SessionAdvice(UserService us){
        this.us = us;
    }

    @ModelAttribute("isLeggedIn")
    public boolean isLoggedIn(){
        return us.isLoggedIn();
    }

    @ModelAttribute("loggedUser")
    public User getLoggedUser(){
        return us.getLoggedUser().orElse(null);
    }
}
