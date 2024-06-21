package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.deals.Deal;
import ar.edu.itba.paw.models.exception.BookNotFoundException;
import ar.edu.itba.paw.models.exception.IllegalReviewException;
import ar.edu.itba.paw.models.orders.Order;
import ar.edu.itba.paw.models.questions.Question;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.reviews.ReviewOrderBy;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.webapp.form.*;
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

    private static final Integer BOOK_INFO_QUESTION_PAGE_SIZE=5;

    private static final Integer QUESTION_PAGE_SIZE=10;
    private final PublishService ps;
    private final BookService bs;
    private final ReviewService rs;
    private final OrderService os;
    private final QuestionService qs;

    private final DealService ds;


    @Autowired
    public BookController(final PublishService ps, final BookService bs, final ReviewService rs, final OrderService os, final QuestionService qs, DealService ds){
        this.ps = ps;
        this.bs = bs;
        this.rs = rs;
        this.os = os;
        this.qs = qs;
        this.ds = ds;
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
    public ModelAndView defaultbookInfo(
            @PathVariable("bookId") final long bookId
    ){
        return new ModelAndView("redirect:/book/" + bookId + "/reviews");
    }

    @RequestMapping(method = RequestMethod.GET, path="/book/{bookId:\\d+}/{tab:myQuestions|questions|reviews}")
    public ModelAndView bookInfo(
            @PathVariable("bookId") final long bookId,
            @PathVariable("tab") String tab,
            @ModelAttribute("loggedUser") User loggedUser,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ModelAttribute("reviewForm") ReviewForm form,
            @ModelAttribute("questionForm") QuestionForm questionForm,
            @ModelAttribute("answerForm") AnswerForm answerForm,
            @Valid @ModelAttribute("reviewSortForm") ReviewSortForm sortForm,
            @Valid @ModelAttribute("dealForm") DealFrom dealForm,
            final BindingResult error
    ){
        if (page < 1){
            page = 1;
        }

        if (error.hasErrors()){
            sortForm.setOrderBy(ReviewOrderBy.DATE_DESC);
        }

        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        List<Book> recommendations = bs.getRecommendations(book);
        PaginatedContent<Question> myQuestions = loggedUser!=null? qs.getAllFromUserAndBook(loggedUser.getUserId(), bookId, page, BOOK_INFO_QUESTION_PAGE_SIZE): null;
        PaginatedContent<Review> reviews = rs.getAll(bookId,sortForm.getOrderBy(), page, REVIEW_PAGE_SIZE);
        Optional<Review> loggedUserReview = rs.findLoggedUserReview(bookId);
        Optional<Order> order = loggedUser!=null? os.find(loggedUser.getUserId(), bookId):Optional.empty();
        int avgRating = rs.getAverageRating(bookId);
        boolean ownsBook = os.loggedUserOwnsBook(bookId);
        boolean isAuthor = loggedUser != null && bs.isAuthor(book, loggedUser.getUserId());
        boolean existsOrder = os.existsOrder(bookId);
        PaginatedContent<Question> questions = qs.getAll(bookId, page, BOOK_INFO_QUESTION_PAGE_SIZE);
        boolean isWishlisted = loggedUser != null && bs.isWishlisted(loggedUser.getUserId(), bookId);

        if (loggedUserReview.isPresent()){
            form.setRating(loggedUserReview.get().getRating());
            form.setReview(loggedUserReview.get().getReview());
        }

        final ModelAndView mav = new ModelAndView("bookInfo");

        mav.addObject("book", book);
        mav.addObject("order", order.orElse(null));
        mav.addObject("questions", questions);
        mav.addObject("myQuestions", myQuestions);
        mav.addObject("recommendations", recommendations);
        mav.addObject("reviews", reviews);
        mav.addObject("loggedUserReview", loggedUserReview.orElse(null));
        mav.addObject("avgRating", avgRating);
        mav.addObject("ownsBook", ownsBook);
        mav.addObject("isAuthor", isAuthor);
        mav.addObject("reviewOrders", List.of(ReviewOrderBy.values()));
        mav.addObject("existsOrder", existsOrder);
        mav.addObject("tab", tab);
        if(tab.matches("reviews")){
            mav.addObject("pageNumber", reviews.getPageNumber());
            mav.addObject("pageCount", reviews.getPageCount());
        }
        if(tab.matches("myQuestions")){
            mav.addObject("pageNumber", myQuestions.getPageNumber());
            mav.addObject("pageCount", myQuestions.getPageCount());
        }
        if(tab.matches("questions")){
            mav.addObject("pageNumber", questions.getPageNumber());
            mav.addObject("pageCount", questions.getPageCount());
        }
        mav.addObject("isWishlisted", isWishlisted);

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


        bs.editPublication(bs.findById(id).orElseThrow(BookNotFoundException::new).getBookId(), form.getTitle(), form.getDescription(), form.getGenre(), form.getPrice(), form.getPageCount(), form.getSuggestedAge(), form.getCover(), form.getPreview(), form.getBookFile());

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


    @RequestMapping(method = RequestMethod.POST, path = "/book/{bookId:\\d+}/reviews/review")
    public ModelAndView createOrUpdateReview(
            @Valid @ModelAttribute("reviewForm") final ReviewForm form,
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

    @RequestMapping(method = RequestMethod.POST, path = "/wishlist/{bookId:\\d+}")
    public ModelAndView toggleWishlist(@ModelAttribute("loggedUser") User user, @PathVariable("bookId") long bookId){
        bs.toggleWishlist(user.getUserId(), bookId);
        return new ModelAndView("redirect:/book/"+bookId);
    }

    @RequestMapping(method = RequestMethod.GET, path="/book/{bookId:\\d+}/deal")
    public ModelAndView addDealForm(@ModelAttribute("dealForm") DealFrom form, @PathVariable("bookId") long bookId){

        ModelAndView mav = new ModelAndView("createDeal");
        Book book = bs.findById(bookId).orElseThrow(BookNotFoundException::new);
        mav.addObject("book", book);
        return mav;
    }



    @RequestMapping(method = RequestMethod.POST, path="/book/{bookId:\\d+}/deal")
    public ModelAndView addDeal(@Valid @ModelAttribute final DealFrom dealForm, final BindingResult errors, @PathVariable("bookId") long bookId){

        if (errors.hasErrors()){
            return addDealForm(dealForm, bookId);
        }

        ds.create(bookId, dealForm.getPrice(), dealForm.getDuration());


        return new ModelAndView("redirect:/book/"+bookId);
    }

    @RequestMapping(method = RequestMethod.POST, path="/book/{bookId:\\d+}/{dealId:\\d+}/endDeal")
    public ModelAndView endDeal(@PathVariable("dealId") long dealId, @PathVariable("bookId") long bookId){

        ds.endDeal(dealId);

        return new ModelAndView("redirect:/book/"+bookId);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/book/{bookId:\\d+}/question")
    public ModelAndView createQuestion(
            @PathVariable("bookId") final long bookId,
            @Valid @ModelAttribute("questionForm") final QuestionForm questionForm,
            final BindingResult error
    ){
        if (error.hasErrors()){
            return new ModelAndView("redirect:/book/"+bookId+"/myQuestions");
        }
        qs.create(bookId, questionForm.getQuestion());
        return new ModelAndView("redirect:/book/"+bookId+"/myQuestions");
    }



    @RequestMapping(method = RequestMethod.POST, path = "/book/{bookId:\\d+}/questions/{questionId:\\d+}/answer")
    public ModelAndView answerQuestion(
            @PathVariable("bookId") final long bookId,
            @Valid @ModelAttribute("answerForm") final AnswerForm answerForm,
            final BindingResult error,
            @PathVariable("questionId") long questionId
    ){
        if (error.hasErrors()){
            return new ModelAndView("redirect:/book/"+bookId+"/questions");
        }
        qs.answer(questionId, answerForm.getAnswer());
        return new ModelAndView("redirect:/book/"+bookId+"/questions");
    }

    @RequestMapping(method = RequestMethod.GET, path="/questions")
    public ModelAndView defaultQuestions(){
        return new ModelAndView("redirect:/questions/myQuestions");
    }

    @RequestMapping(method = RequestMethod.GET, path="/questions/{tab:myQuestions|questions}")
    public ModelAndView questions(
            @ModelAttribute("loggedUser") User user,
            @PathVariable("tab") String tab,
            @RequestParam(value = "page", defaultValue = "1") Integer page,
            @ModelAttribute("answerForm") AnswerForm answerForm
    ){
        PaginatedContent<Question> myQuestions = qs.getAllFromUser(user.getUserId(), page, QUESTION_PAGE_SIZE);
        PaginatedContent<Question> questions = qs.getAllFromWriter(user.getUserId(), page, QUESTION_PAGE_SIZE);
        ModelAndView mav = new ModelAndView("questions");
        mav.addObject("myQuestions", myQuestions);
        mav.addObject("questions", questions);
        mav.addObject("tab", tab);
        mav.addObject("isAuthor", user.getRoles().contains(UserRoles.WRITER));
        if(tab.matches("myQuestions")){
            mav.addObject("pageNumber", myQuestions.getPageNumber());
            mav.addObject("pageCount", myQuestions.getPageCount());
        }
        if(tab.matches("questions")){
            mav.addObject("pageNumber", questions.getPageNumber());
            mav.addObject("pageCount", questions.getPageCount());
        }
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/questions/questions/{questionId:\\d+}/answer")
    public ModelAndView answerQuestion2(
            @Valid @ModelAttribute("answerForm") final AnswerForm answerForm,
            final BindingResult error,
            @PathVariable("questionId") long questionId
    ){
        if (error.hasErrors()){
            return new ModelAndView("redirect:/questions/questions");
        }
        qs.answer(questionId, answerForm.getAnswer());
        return new ModelAndView("redirect:/questions/questions");
    }


}

