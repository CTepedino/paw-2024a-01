package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.IllegalSearchQueryException;
import ar.edu.itba.paw.webapp.form.BookSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;


@Controller
public class HomeController {

    private static final int PAGE_SIZE = 20;

    private final BookService bs;

    @Autowired
    public HomeController(BookService bs){
        this.bs = bs;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView home(@RequestParam(name = "page", defaultValue = "1") Integer page){

        PaginatedContent<Book> books = bs.getAll(page, PAGE_SIZE);
        List<Book> bestSellers = bs.getTopBooks();

        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("books", books);
        mav.addObject("popularGenres", bs.getGenresByBookCount());
        mav.addObject("bestSellers", bestSellers);
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="/search")
    public ModelAndView search(@Valid @ModelAttribute("bookSearchForm") BookSearchForm form, final BindingResult error){
        Integer page = form.getPage();
        page = page == null ? 1 : page;
        if (error.hasErrors()){
            throw new IllegalSearchQueryException();
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
                page,
                PAGE_SIZE
        );

        mav.addObject("title", form.getTitle());
        mav.addObject("books", books);
        mav.addObject("genres", BookGenre.values());
        mav.addObject("orders", BookSearchOrderBy.values());

        return mav;
    }
    
}
