package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.PublishService;
import ar.edu.itba.paw.webapp.form.NewBookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class BookController {

    private final PublishService ps;

    @Autowired
    public BookController(PublishService ps){
        this.ps = ps;
    }

    @RequestMapping(method = RequestMethod.GET, path="/addBook")
    public ModelAndView addBookForm(){
        return new ModelAndView("addBook");
    }

    @RequestMapping(method = RequestMethod.POST, path="/addBook")
    public ModelAndView addBook(@ModelAttribute final NewBookForm newBookForm){

        ps.publishBook(
                newBookForm.getWriterFirstName(),
                newBookForm.getWriterLastName(),
                newBookForm.getWriterEmail(),

                newBookForm.getTitle(),
                newBookForm.getDescription(),
                newBookForm.getGenre(),
                newBookForm.getSuggestedAge(),
                newBookForm.getPrice(),
                newBookForm.getPageCount(),

                newBookForm.getImage(),
                newBookForm.getPdf()
        );

        return new ModelAndView("redirect:/");
    }

}
