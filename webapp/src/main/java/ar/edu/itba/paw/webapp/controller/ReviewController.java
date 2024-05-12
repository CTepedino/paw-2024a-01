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




}
