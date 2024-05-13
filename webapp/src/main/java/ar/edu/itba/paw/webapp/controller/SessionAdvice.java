package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SessionAdvice {

    private final UserService us;

    @Autowired
    public SessionAdvice(UserService us){
        this.us = us;
    }

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(){
        return us.isLoggedIn();
    }

    @ModelAttribute("loggedUser")
    public User getLoggedUser(){
        return us.getLoggedUser().orElse(null);
    }

    @ModelAttribute("isWriter")
    public boolean isWriter(){
        return us.hasRole(UserRoles.WRITER);
    }
}
