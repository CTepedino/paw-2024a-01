package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.webapp.form.MyBookSearchForm;
import ar.edu.itba.paw.webapp.form.NewBookForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class BookController {

    private final static Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final PublishService ps;
    private final BookService bs;
    private final ReviewService rs;
    private final OrderService os;
    private final UserService us;


    @Autowired
    public BookController(final PublishService ps, final BookService bs, final UserService us, final ReviewService rs, final OrderService os){
        this.ps = ps;
        this.bs = bs;
        this.us = us;
        this.rs = rs;
        this.os = os;
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
    public ModelAndView bookInfo(
            @PathVariable("bookId") final long bookId,
            @RequestParam(value = "reviewPage", defaultValue = "1") Integer reviewPage
    ){
        final ModelAndView mav = new ModelAndView("bookInfo");

        boolean ownsBook = os.loggedUserOwnsBook(bookId);
        boolean isAuthor = bs.loggedUserIsAuthor(bookId);

        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        List<Book> recommendations = bs.getRecommendations(book);

        PaginatedContent<Review> reviews = rs.getAll(bookId, ReviewOrderBy.DATE_DESC, reviewPage,20);
        int avgRating = rs.getAverageRating(bookId);

        Optional<Review> loggedUserReview = rs.findLoggedUserReview(bookId);

        mav.addObject("book", book);
        mav.addObject("recommendations", recommendations);
        mav.addObject("reviews", reviews);
        mav.addObject("avgRating", avgRating);
        mav.addObject("ownsBook", ownsBook);
        mav.addObject("isAuthor", isAuthor);
        mav.addObject("loggedUserReview", loggedUserReview);
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="myBooks")
    public ModelAndView myBooks(@Valid @ModelAttribute("myBooksSearchForm") final MyBookSearchForm form, final BindingResult error){

        if(error.hasErrors()){
            return new ModelAndView("myBooks");
        }

        ModelAndView mav = new ModelAndView("myBooks");
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);
        mav.addObject("books", bs.getWriterBooksWithParams(user.getUserId(), form.getTitle(), form.getOrderBy(),1, 20));
        mav.addObject("myBookSearchForm", form);
        mav.addObject("orders", BookSearchOrderBy.values());
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="/book/edit/{id:\\d+}")
    public ModelAndView editBookForm(@ModelAttribute("editBookForm") NewBookForm form, @PathVariable("id") long id){

        Book book = bs.findById(id).orElseThrow(BookNotFoundException::new);
        form.setTitle(book.getTitle());
        form.setDescription(book.getDescription());
        form.setGenre(book.getGenre());
        form.setPrice(book.getPrice());
        form.setPageCount(book.getPageCount());
        form.setSuggestedAge(book.getSuggestedAge());

        ModelAndView mav = new ModelAndView("editBook");
        mav.addObject("id", id);
        mav.addObject("genres", BookGenre.values());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path="/book/edit/{id:\\d+}")
    public ModelAndView editBook(@Valid @ModelAttribute("editBookForm") NewBookForm form, final BindingResult error, @PathVariable("id") long id){

        if (error.hasErrors()){
            return editBookForm(form, id);
        }

        bs.editPublication(id, form.getTitle(), form.getDescription(), form.getGenre(), form.getPrice(), form.getPageCount(), form.getSuggestedAge(), form.getCover(), form.getPreview(), form.getBookFile());

        return new ModelAndView("redirect:/book/"+id);
    }

}

