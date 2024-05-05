package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.PublishService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.webapp.form.BookSearchForm;
import ar.edu.itba.paw.webapp.form.NewBookForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.webapp.util.SecurityUtils;

import javax.validation.Valid;


@Controller
public class BookController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private static final int PAGE_SIZE = 20;

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
    public ModelAndView home(@RequestParam(name = "page", defaultValue = "1") Integer page){

        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("books", bs.getAll(page, PAGE_SIZE));
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="/book/{bookId:\\d+}")
    public ModelAndView bookInfo(@PathVariable("bookId") final long bookId){
        final ModelAndView mav = new ModelAndView("bookInfo");
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        List<Book> recommendations = bs.getAllGenreExcluding(book.getGenre(), book);
        mav.addObject("book", book);
        mav.addObject("user", us.getLoggedUser().orElse(null));
        mav.addObject("hasWriterRole", SecurityUtils.hasRole("WRITER"));
        mav.addObject("recommendations", recommendations);
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/addBook")
    public ModelAndView addBookForm(@ModelAttribute("newBookForm") NewBookForm form){

        boolean hasWriterRole = SecurityUtils.hasRole("WRITER");

        ModelAndView mav = new ModelAndView("addBook");
        mav.addObject("genres", BookGenre.values());
        mav.addObject("hasWriterRole", hasWriterRole);
        return mav;
    }



    @RequestMapping(method = RequestMethod.POST, path="/addBook")
    public ModelAndView addBook(@Valid @ModelAttribute final NewBookForm newBookForm, final BindingResult errors){

        if (errors.hasErrors()){
            return addBookForm(newBookForm);
        }

        ps.publishBook(
                newBookForm.getTitle(),
                newBookForm.getDescription(),
                newBookForm.getGenre(),
                newBookForm.getSuggestedAge(),
                newBookForm.getPrice(),
                newBookForm.getPageCount(),

                newBookForm.getImage(),
                newBookForm.getPdf()
        );

        LOGGER.atDebug().setMessage("Created the book {}").addArgument(newBookForm::getTitle).log();

        return new ModelAndView("redirect:/");
    }


    @RequestMapping(method = RequestMethod.GET, path="/search")
    public ModelAndView search(@Valid @ModelAttribute("bookSearchForm") final BookSearchForm form, final BindingResult error){

        if (error.hasErrors()){
            return new ModelAndView("searchResults");
        }

        final ModelAndView mav = new ModelAndView("searchResults");

        PaginatedContent<Book> books = bs.searchWithParams(
                form.getTitle(),
                form.getGenre(),
                form.getMinPrice(),
                form.getMaxPrice(),
                form.getMinPageCount(),
                form.getMaxPageCount(),
                form.getMinSuggestedAge(),
                form.getMaxSuggestedAge(),
                form.getOrderBy(),
                form.getPage(),
                PAGE_SIZE
        );

        mav.addObject("bookSearchForm", form);
        mav.addObject("books", books);
        mav.addObject("genres", BookGenre.values());
        mav.addObject("orders", BookSearchOrderBy.values());

        return mav;
    }

}
