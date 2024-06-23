package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.AnalyticsService;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookSalesCategory;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.models.users.WriterCategory;
import ar.edu.itba.paw.webapp.form.AnalyticsForm;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import ar.edu.itba.paw.webapp.form.OrderSearchForm;
import ar.edu.itba.paw.webapp.form.ProfileBookSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.Year;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProfileController {

    private final UserService us;
    private final BookService bs;

    private final AnalyticsService as;
    private static final int PROFILE_PAGE_SIZE = 20;

    private static final int ANALYTICS_PAGE_SIZE = 5;


    @Autowired
    public ProfileController(final UserService us, final BookService bs, AnalyticsService as) {
        this.us = us;
        this.bs = bs;
        this.as = as;
    }


    @RequestMapping(method = RequestMethod.GET, path="/profile")
    public ModelAndView loggedProfile(
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("isWriter") boolean isWriter,
            @ModelAttribute("profileBookSearchForm") final ProfileBookSearchForm form
    ){
        return new ModelAndView("redirect:/profile/" + loggedUser.getUserId() + (isWriter?"/publications":"/boughtBooks"));
    }

    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}")
    public ModelAndView defaultProfileTab(
            @PathVariable("userId") long id,
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("profileBookSearchForm") final ProfileBookSearchForm form
    ){
        return new ModelAndView("redirect:/profile/" + id + (us.findById(id).orElseThrow(UserNotFoundException::new).getRoles().contains(UserRoles.WRITER)?"/publications":"/boughtBooks"));
    }

    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}/{tab:publications|boughtBooks}")
    public ModelAndView profileView(
        @PathVariable("userId") long userId,
        @PathVariable("tab") String tab,
        @ModelAttribute("loggedUser") User loggedUser,
        @Valid @ModelAttribute("profileBookSearchForm") ProfileBookSearchForm form,
        final BindingResult error
    ){
        if(error.hasErrors()){
            if (error.hasFieldErrors("page")){
                form.setPage(1);
            }
            form.setOrderBy(BookSearchOrderBy.PUBLICATION_DATE_DESC);
        }

        User user = us.findById(userId).orElseThrow(UserNotFoundException::new);
        boolean ownsProfile = loggedUser!=null && loggedUser.getUserId()==userId;

        PaginatedContent<Book> books = bs.getProfileBooks(userId, form.getTitle(), form.getOrderBy(), form.getPage(), PROFILE_PAGE_SIZE, tab.equals("publications"), ownsProfile);

        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("tab", tab);
        mav.addObject("user", user);
        mav.addObject("showPublicationsTab", user.getRoles().contains(UserRoles.WRITER));
        mav.addObject("ownsProfile", ownsProfile);
        mav.addObject("books", books);
        mav.addObject("orders", BookSearchOrderBy.values());
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/profile/{userId:\\d+}/wishlist")
    public ModelAndView wishlistView(
            @PathVariable("userId") long userId,
            @RequestParam(name = "page", defaultValue = "1") Integer page
    ){
        PaginatedContent<Book> wishlist = bs.getWishlist(userId, page, PROFILE_PAGE_SIZE);

        final ModelAndView mav = new ModelAndView("wishlist");
        mav.addObject("wishlist", wishlist);
        mav.addObject("tab", "wishlist");
        mav.addObject("ownsProfile", true);
        mav.addObject("showPublicationsTab", us.hasRole(UserRoles.WRITER));
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/editProfile")
    public ModelAndView editProfileForm(@ModelAttribute("editProfileForm") EditProfileForm form, @ModelAttribute("loggedUser") User loggedUser){

        form.setNewFirstName(loggedUser.getFirstName());
        form.setNewLastName(loggedUser.getLastName());
        form.setCbu(loggedUser.getCbu());
        form.setDescription(loggedUser.getDescription());

        return new ModelAndView("editProfile");
    }

    @RequestMapping(method = RequestMethod.POST, path ="/editProfile")
    public ModelAndView editProfile(
            @Valid @ModelAttribute("editProfileForm") EditProfileForm form,
            final BindingResult errors,
            @ModelAttribute("loggedUser") User loggedUser
    ){
        if (errors.hasErrors()){
            return editProfileForm(form, loggedUser);
        }

        us.updateProfile(form.getNewFirstName(),form.getNewLastName(),form.getCbu(), form.getProfilePicture(), form.getDescription());

        return new ModelAndView("redirect:/profile/"+loggedUser.getUserId());
    }

    @RequestMapping(method = RequestMethod.GET, path="/analytics")
    public ModelAndView analytics(
            @ModelAttribute("loggedUser") User loggedUser,
            @Valid @ModelAttribute("analyticsForm") AnalyticsForm analyticsForm,
            final BindingResult error)
    {
        final ModelAndView mav = new ModelAndView("writerDashboard");

        if(error.hasErrors()){
            if (error.hasFieldErrors("page")){
                analyticsForm.setPage(1);
            }
        }
        mav.addObject("user", loggedUser);
        mav.addObject("bronzeMin", WriterCategory.BRONZE.getMinSales());
        mav.addObject("silverMin", WriterCategory.SILVER.getMinSales());
        mav.addObject("goldMin", WriterCategory.GOLD.getMinSales());
        mav.addObject("bestSellerMin", BookSalesCategory.BEST_SELLER.getMinSales());
        mav.addObject("popularMin", BookSalesCategory.POPULAR.getMinSales());
        mav.addObject("books", as.getBooksByWriterWithAnalytics(loggedUser.getUserId(), analyticsForm.byMonth(), analyticsForm.getMonth(), analyticsForm.getYear(), analyticsForm.getPage(), ANALYTICS_PAGE_SIZE));
        mav.addObject("totalRevenue", as.getTotalSales(loggedUser.getUserId()));
        mav.addObject("totalOrders", as.getTotalOrdersForWriter(loggedUser.getUserId()));
        mav.addObject("totalRevenueThisMonth", as.getTotalSalesForMonth(loggedUser.getUserId(), YearMonth.now().getYear(), YearMonth.now().getMonthValue()));
        mav.addObject("totalOrdersThisMonth", as.getTotalOrdersForWriterForMonth(loggedUser.getUserId(), YearMonth.now().getYear(), YearMonth.now().getMonthValue()));
        mav.addObject("years", as.getYears());
        mav.addObject("months", as.getMonths());
        mav.addObject("showMonths", analyticsForm.byMonth());
        mav.addObject("revenueChange", as.getSalesIncrease(loggedUser.getUserId()));
        mav.addObject("ordersChange", as.getOrdersIncrease(loggedUser.getUserId()));
        mav.addObject("totalRevenueThatMonth", as.getTotalSalesForMonth(loggedUser.getUserId(), analyticsForm.getYear(), analyticsForm.getMonth()));
        mav.addObject("totalOrdersThatMonth", as.getTotalOrdersForWriterForMonth(loggedUser.getUserId(), analyticsForm.getYear(), analyticsForm.getMonth()));
        return mav;
    }

}