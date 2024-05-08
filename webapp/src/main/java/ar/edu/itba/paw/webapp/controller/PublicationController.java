package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.PublishService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.webapp.form.NewBookForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class PublicationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PublicationController.class);

    private final PublishService ps;
    private final BookService bs;

    @Autowired
    public PublicationController(final PublishService ps, final BookService bs){
        this.ps = ps;
        this.bs = bs;
    }

    @RequestMapping(method = RequestMethod.GET, path="/addBook")
    public ModelAndView addBookForm(@ModelAttribute("newBookForm") NewBookForm form){


        ModelAndView mav = new ModelAndView("addBook");
        mav.addObject("genres", BookGenre.values());
        return mav;
    }



    @RequestMapping(method = RequestMethod.POST, path="/addBook")
    public ModelAndView addBook(@Valid @ModelAttribute final NewBookForm newBookForm, final BindingResult errors){

        if (errors.hasErrors()){
            return addBookForm(newBookForm);
        }

        long bookId = ps.publishBook(
                newBookForm.getCbu(),
                newBookForm.getTitle(),
                newBookForm.getDescription(),
                newBookForm.getGenre(),
                newBookForm.getSuggestedAge(),
                newBookForm.getPrice(),
                newBookForm.getPageCount(),

                newBookForm.getCover(),
                newBookForm.getPreview(),
                newBookForm.getBookFile()
        );

        LOGGER.atDebug().setMessage("Created the book {}").addArgument(newBookForm::getTitle).log();

        return new ModelAndView("redirect:/book/"+bookId);
    }

    @RequestMapping(method = RequestMethod.GET, path="/book/{bookId:\\d+}")
    public ModelAndView bookInfo(@PathVariable("bookId") final long bookId){
        final ModelAndView mav = new ModelAndView("bookInfo");
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        List<Book> recommendations = bs.getAllGenreExcluding(book.getGenre(), book);
        mav.addObject("book", book);
        mav.addObject("recommendations", recommendations);
        return mav;
    }

    //TODO: edit book

}

