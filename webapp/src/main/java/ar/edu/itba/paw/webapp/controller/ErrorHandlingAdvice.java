package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandlingAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView bookNotFound(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(IllegalReviewException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView illegalReview(){return new ModelAndView("forward:/400");}

    @ExceptionHandler(IllegalSearchQueryException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView illegalSearchQuery(){return new ModelAndView("forward:/400");}

    @ExceptionHandler(ImageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView imageNotFound(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(InvalidCodeException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView invalidCode(){return new ModelAndView("forward:/403");}

    @ExceptionHandler(InvalidOrderUpdateException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView invalidOrderUpdate(){return new ModelAndView("forward:/400");}

    @ExceptionHandler(InvalidPageException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView invalidPage(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(NoValidationCodeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView noValidationCode(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(OrderAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView orderExists(){return new ModelAndView("forward:/403");}

    @ExceptionHandler(OrderNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView orderNotFound(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(PdfNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView pdfNotFound(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(SameWriterAndBuyerException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView buyingOwnBook(){return new ModelAndView("forward:/403");}

    @ExceptionHandler(UnreadableFileException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView unreadableFile(){return new ModelAndView("forward:/400");}

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound(){return new ModelAndView("forward:/404");}

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exceptionCatchAll(){return new ModelAndView("forward:/500");}

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView maxUploadSizeExceeded() {return new ModelAndView("forward:/400");}

}
