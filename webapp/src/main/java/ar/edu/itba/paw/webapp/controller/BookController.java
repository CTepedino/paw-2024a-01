package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.IllegalReviewException;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.webapp.form.EditBookForm;
import ar.edu.itba.paw.webapp.form.NewBookForm;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import ar.edu.itba.paw.webapp.form.ReviewSortForm;
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

    private final Integer REVIEW_PAGE_SIZE=10;

    private final static Logger LOGGER = LoggerFactory.getLogger(BookController.class);

    private final PublishService ps;
    private final BookService bs;
    private final ReviewService rs;
    private final OrderService os;


    @Autowired
    public BookController(final PublishService ps, final BookService bs, final ReviewService rs, final OrderService os){
        this.ps = ps;
        this.bs = bs;
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


        return new ModelAndView("redirect:/book/"+bookId);
    }


    @RequestMapping(method = RequestMethod.GET, path="/book/{bookId:\\d+}")
    public ModelAndView bookInfo(
            @PathVariable("bookId") final long bookId,
            @RequestParam(value = "reviewPage", defaultValue = "1") Integer reviewPage,
            @ModelAttribute("reviewForm") ReviewForm form,
            @Valid @ModelAttribute("reviewSortForm") ReviewSortForm sortForm,
            final BindingResult error
    ){
        if (reviewPage < 1){
            reviewPage = 1;
        }

        if (error.hasErrors()){
            sortForm.setOrderBy(ReviewOrderBy.DATE_DESC);
        }

        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        List<Book> recommendations = bs.getRecommendations(book);
        PaginatedContent<Review> reviews = rs.getAll(bookId,sortForm.getOrderBy(), reviewPage, REVIEW_PAGE_SIZE);
        Optional<Review> loggedUserReview = rs.findLoggedUserReview(bookId);
        int avgRating = rs.getAverageRating(bookId);
        boolean ownsBook = os.loggedUserOwnsBook(bookId);
        boolean isAuthor = bs.loggedUserIsAuthor(bookId);
        boolean canBuy = os.canCreateOrder(bookId);

        if (loggedUserReview.isPresent()){
            form.setRating(loggedUserReview.get().getRating());
            form.setReview(loggedUserReview.get().getReview());
        }

        final ModelAndView mav = new ModelAndView("bookInfo");

        mav.addObject("book", book);
        mav.addObject("recommendations", recommendations);
        mav.addObject("reviews", reviews);
        mav.addObject("loggedUserReview", loggedUserReview.orElse(null));
        mav.addObject("avgRating", avgRating);
        mav.addObject("ownsBook", ownsBook);
        mav.addObject("isAuthor", isAuthor);
        mav.addObject("reviewOrders", List.of(ReviewOrderBy.values()));
        mav.addObject("canBuy", canBuy);
        return mav;
    }



    @RequestMapping(method = RequestMethod.GET, path="/book/edit/{id:\\d+}")
    public ModelAndView editBookForm(@ModelAttribute("editBookForm") EditBookForm form, @PathVariable("id") long id){

        Book book = bs.findById(id).orElseThrow(BookNotFoundException::new);

        form.setTitle(book.getTitle());
        form.setDescription(book.getDescription());
        form.setGenre(book.getGenre());
        form.setPrice(book.getPrice());
        form.setPageCount(book.getPageCount());
        form.setSuggestedAge(book.getSuggestedAge());
        form.setIsPaused(book.isPaused());

        ModelAndView mav = new ModelAndView("editBook");
        mav.addObject("id", id);
        mav.addObject("genres", BookGenre.values());
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path="/book/edit/{id:\\d+}")
    public ModelAndView editBook(@Valid @ModelAttribute("editBookForm") EditBookForm form, final BindingResult error, @PathVariable("id") long id){

        if (error.hasErrors()){
            return editBookForm(form, id);
        }

        bs.editPublication(id, form.getTitle(), form.getDescription(), form.getGenre(), form.getPrice(), form.getPageCount(), form.getSuggestedAge(), form.getIsPaused(), form.getCover(), form.getPreview(), form.getBookFile());

        return new ModelAndView("redirect:/book/"+id);
    }


    @RequestMapping(method = RequestMethod.POST, path = "/book/{bookId:\\d+}/review")
    public ModelAndView createOrUpdateReview(
            @Valid @ModelAttribute("reviewForm") ReviewForm form,
            final BindingResult error,
            @PathVariable("bookId") long bookId,
            @ModelAttribute("loggedUser") User user
    ){

        if (error.hasErrors()){
            throw new IllegalReviewException();
        }

        rs.createOrUpdate(bookId, user.getUserId(), form.getRating(), form.getReview());

        return new ModelAndView("redirect:/book/"+bookId);
    }


}

