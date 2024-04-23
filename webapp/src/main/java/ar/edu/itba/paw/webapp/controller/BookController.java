package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.PublishService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.BookGenre;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.webapp.form.NewBookForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.webapp.util.SecurityUtils;

import javax.validation.Valid;


@Controller
public class BookController {

    private final PublishService ps;
    private final BookService bs;
    private final UserService us;


    @Autowired
    public BookController(PublishService ps, BookService bs, UserService us){
        this.ps = ps;
        this.bs = bs;
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView home(){

        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("books", bs.getAll());
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/{bookId:\\d+}")
    public ModelAndView bookInfo(@PathVariable("bookId") final long bookId){
        final ModelAndView mav = new ModelAndView("bookInfo");
        mav.addObject("book", bs.findById(bookId).orElseThrow(BookNotFoundException::new));
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/addBook")
    public ModelAndView addBookForm(){

        boolean hasWriterRole = SecurityUtils.hasRole("WRITER");

        if (!hasWriterRole){
            return new ModelAndView("redirect:/signup/writer");
        }
    public ModelAndView addBookForm(@ModelAttribute("newBookForm") NewBookForm form){
        ModelAndView mav = new ModelAndView("addBook");
        mav.addObject("genres", BookGenre.values());
        mav.addObject("hasWriterRole", hasWriterRole);
        mav.addObject("newBookForm", form);
        return mav;
    }



    @RequestMapping(method = RequestMethod.POST, path="/addBook")
    public ModelAndView addBook(@Valid @ModelAttribute final NewBookForm newBookForm, final BindingResult errors){

        if (errors.hasErrors()){
            return addBookForm(newBookForm);
        }
    public ModelAndView addBook(@ModelAttribute("newBookForm") final NewBookForm newBookForm){

        ps.publishBook(
               /* newBookForm.getWriterFirstName(),
                newBookForm.getWriterLastName(),
                newBookForm.getWriterEmail(),*/
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
