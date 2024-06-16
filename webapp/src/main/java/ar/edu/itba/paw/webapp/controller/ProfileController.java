package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookAndDeal;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import ar.edu.itba.paw.webapp.form.ProfileBookSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class ProfileController {

    private final UserService us;
    private final BookService bs;

    private static final int PROFILE_PAGE_SIZE = 20;


    @Autowired
    public ProfileController(final UserService us, final BookService bs) {
        this.us = us;
        this.bs = bs;
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

        PaginatedContent<BookAndDeal> books = bs.getProfileBooksWithDeals(userId, form.getTitle(), form.getOrderBy(), form.getPage(), PROFILE_PAGE_SIZE, tab.equals("publications"), ownsProfile);

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
}
