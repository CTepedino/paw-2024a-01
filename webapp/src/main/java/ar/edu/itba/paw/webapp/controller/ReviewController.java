package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaces.service.ReviewService;
import ar.edu.itba.paw.models.reviews.Review;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.webapp.form.ReviewForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class ReviewController {

    private final ReviewService rs;

    @Autowired
    public ReviewController(ReviewService rs) {
        this.rs = rs;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/book/{bookId:\\d+}/review/")
    public ModelAndView createOrUpdateReviewForm(@ModelAttribute("reviewForm") ReviewForm form, @PathVariable("bookId") long bookId){

        Optional<Review> maybeReview = rs.findLoggedUserReview(bookId);
        if (maybeReview.isPresent()){
            form.setReview(maybeReview.get().getReview());
            form.setRating(maybeReview.get().getRating());
        }

        ModelAndView mav = new ModelAndView("reviewForm");
        mav.addObject("bookId", bookId);
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/book/{bookId:\\d+}/review/")
    public ModelAndView createOrUpdateReview(
            @Valid @ModelAttribute("reviewForm") ReviewForm form,
            final BindingResult error,
            @PathVariable("bookId") long bookId,
            @ModelAttribute("loggedUser") User user
    ){

        if (error.hasErrors()){
            return createOrUpdateReviewForm(form, bookId);
        }

        rs.createOrUpdate(bookId, user.getUserId(), form.getRating(), form.getReview());

        return new ModelAndView("redirect:book/"+bookId);
    }

}
