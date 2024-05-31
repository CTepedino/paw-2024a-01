package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.IllegalReviewException;
import ar.edu.itba.paw.models.orders.Order;
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

    private static final Integer REVIEW_PAGE_SIZE=5;

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
    public ModelAndView addBook(@Valid @ModelAttribute final NewBookForm newBookForm, final BindingResult errors, @ModelAttribute("loggedUser") User loggedUser){

        if (errors.hasErrors()){
            return addBookForm(newBookForm);
        }

        long bookId = ps.publishBook(
                loggedUser,

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
            @ModelAttribute("loggedUser") User loggedUser,
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
        Optional<Order> order = loggedUser!=null? os.find(loggedUser.getUserId(), bookId):Optional.empty();
        int avgRating = rs.getAverageRating(bookId);
        boolean ownsBook = os.loggedUserOwnsBook(bookId);
        boolean isAuthor = loggedUser != null && bs.isAuthor(book, loggedUser.getUserId());
        boolean existsOrder = os.existsOrder(bookId);

        if (loggedUserReview.isPresent()){
            form.setRating(loggedUserReview.get().getRating());
            form.setReview(loggedUserReview.get().getReview());
        }

        final ModelAndView mav = new ModelAndView("bookInfo");

        mav.addObject("book", book);
        mav.addObject("order", order.orElse(null));
        mav.addObject("recommendations", recommendations);
        mav.addObject("reviews", reviews);
        mav.addObject("loggedUserReview", loggedUserReview.orElse(null));
        mav.addObject("avgRating", avgRating);
        mav.addObject("ownsBook", ownsBook);
        mav.addObject("isAuthor", isAuthor);
        mav.addObject("reviewOrders", List.of(ReviewOrderBy.values()));
        mav.addObject("existsOrder", existsOrder);
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


        bs.editPublication(bs.findById(id).orElseThrow(BookNotFoundException::new), form.getTitle(), form.getDescription(), form.getGenre(), form.getPrice(), form.getPageCount(), form.getSuggestedAge(), form.getCover(), form.getPreview(), form.getBookFile());

        return new ModelAndView("redirect:/book/"+id);
    }


    @RequestMapping(method = RequestMethod.POST, path="/recommendBook/{id:\\d+}/bookInfo")
    public ModelAndView recommendBook(
            @RequestParam(name = "recommended", required = false, defaultValue = "false") boolean recommended,
            @PathVariable long id
    ){
        os.recommendBook(id, recommended);
        return new ModelAndView("redirect:/book/"+os.findById(id).orElseThrow(BookNotFoundException::new).getBook().getBookId());
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

        rs.createOrUpdate(bookId, user, form.getRating(), form.getReview());

        return new ModelAndView("redirect:/book/"+bookId);
    }


}

