package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.models.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class ErrorHandlingAdvice {


    @ExceptionHandler({
            BookNotFoundException.class,
            QuestionNotFoundException.class,
            ImageNotFoundException.class,
            PdfNotFoundException.class,
            OrderNotFoundException.class,
            UserNotFoundException.class,
            NoValidationCodeException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView notFound(){return new ModelAndView("exception/404");}

    @ExceptionHandler({
            IllegalReviewException.class,
            IllegalSearchQueryException.class,
            InvalidOrderUpdateException.class,
            InvalidPageException.class,
            UnreadableFileException.class,
            MaxUploadSizeExceededException.class
    })
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequest(){return new ModelAndView("exception/400");}

    @ExceptionHandler({
            InvalidCodeException.class,
            OrderAlreadyExistsException.class,
            SameWriterAndBuyerException.class
    })
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView forbidden(){return new ModelAndView("exception/403");}

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView exceptionCatchAll(){return new ModelAndView("exception/500");}



}
