package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.PublishService;
import ar.edu.itba.paw.models.BookGenre;
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
    private final BookService bs;

    @Autowired
    public BookController(PublishService ps, BookService bs){
        this.ps = ps;
        this.bs = bs;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView home(){
        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("books", bs.getAll());
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/addBook")
    public ModelAndView addBookForm(){
        ModelAndView mav = new ModelAndView("addBook");
        mav.addObject("genres", BookGenre.values());
        return mav;
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
