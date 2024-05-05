package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.exception.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView userNotFound(){
        return new ModelAndView("exception/404");
    }

    @ExceptionHandler(ImageNotFoundException.class)
    public ModelAndView imageNotFound(){return new ModelAndView("/exception/404");}

    @ExceptionHandler(InvalidPageException.class)
    public ModelAndView invalidPage(){return new ModelAndView("/exception/500");}

    @ExceptionHandler(OrderAlreadyExistsException.class)
    public ModelAndView orderExists(){return new ModelAndView("/exception/403");}

    @ExceptionHandler(OrderNotFoundException.class)
    public ModelAndView orderNotFound(){return new ModelAndView("/exception/404");}

    @ExceptionHandler(PdfNotFoundException.class)
    public ModelAndView pdfNotFound(){return new ModelAndView("/exception/404");}

    @ExceptionHandler(SameWriterAndBuyerException.class)
    public ModelAndView buyingOwnBook(){return new ModelAndView("/exception/403");}

    @ExceptionHandler(UnreadableFileException.class)
    public ModelAndView unreadableFile(){return new ModelAndView("/exception/400");}


}
