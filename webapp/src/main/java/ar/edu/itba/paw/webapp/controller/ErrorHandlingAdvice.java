package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView userNotFound(){
        return new ModelAndView("exception/userNotFound");
    }
}
