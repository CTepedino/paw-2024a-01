package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import ar.edu.itba.paw.webapp.form.EditProfileForm;
import ar.edu.itba.paw.webapp.form.ProfileBookSearchForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
        return defaultProfileTab(loggedUser.getUserId(), loggedUser, form);
    }

    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}")
    public ModelAndView defaultProfileTab(
            @PathVariable("userId") long id,
            @ModelAttribute("loggedUser") User loggedUser,
            @ModelAttribute("profileBookSearchForm") final ProfileBookSearchForm form
    ){
        return profileView(id, us.hasRole(UserRoles.WRITER)?"publications":"boughtBooks", loggedUser, form, );
    }

/*    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}")
    public ModelAndView profile(@PathVariable("userId") final long userId, @ModelAttribute("loggedUser") User loggedUser){
        final ModelAndView mav = new ModelAndView("profile");

        User user = us.findById(userId).orElseThrow(UserNotFoundException::new);
        boolean ownsProfile = loggedUser!=null && loggedUser.getUserId()==userId;

        mav.addObject("user", user);
        mav.addObject("ownsProfile", ownsProfile);
        mav.addObject("publicationSelected", false);
        mav.addObject("boughtBooksSelected", false);
        return mav;
    }*/


    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}}/{tab:publications|boughtBooks}")
    public ModelAndView profileView(
        @PathVariable("userId") long userId,
        @PathVariable("tab") String tab,
        @ModelAttribute("loggedUser") User loggedUser,
        @Valid @ModelAttribute("profileBookSearchForm") final ProfileBookSearchForm form,
        final BindingResult error
    ){
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("tab", tab);

    }

    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}/publications")
    public ModelAndView publications(
            @PathVariable("userId") final long userId,
            @ModelAttribute("loggedUser") User loggedUser,
            @Valid @ModelAttribute("myBooksSearchForm") final ProfileBookSearchForm form,
            final BindingResult error){

        if(error.hasErrors()){
            return new ModelAndView("publications");
        }

        ModelAndView mav = new ModelAndView("publications");
        User user = us.findById(userId).orElseThrow(UserNotFoundException::new);
        boolean ownsProfile = loggedUser!=null && loggedUser.getUserId()==userId;
        mav.addObject("user", user);
        mav.addObject("ownsProfile", ownsProfile);
        mav.addObject("publicationSelected", true);
        mav.addObject("boughtBooksSelected", false);
        mav.addObject("books", bs.getWriterBooks(userId, form.getTitle(), form.getOrderBy(), form.getPage(), PROFILE_PAGE_SIZE));
        mav.addObject("myBookSearchForm", form);
        mav.addObject("orders", BookSearchOrderBy.values());
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="/profile/{userId:\\d+}/boughtBooks")
    public ModelAndView boughtBooks(
            @PathVariable("userId") final long userId,
            @ModelAttribute("loggedUser") User loggedUser,
            @Valid @ModelAttribute("myBooksSearchForm") final ProfileBookSearchForm form,
            final BindingResult error){

        if(error.hasErrors()){
            return new ModelAndView("boughtBooks");
        }

        ModelAndView mav = new ModelAndView("boughtBooks");
        User user = us.findById(userId).orElseThrow(UserNotFoundException::new);
        boolean ownsProfile = loggedUser!=null && loggedUser.getUserId()==userId;
        mav.addObject("user", user);
        mav.addObject("ownsProfile", ownsProfile);
        mav.addObject("publicationSelected", false);
        mav.addObject("boughtBooksSelected", true);
        mav.addObject("books", bs.getOwnedBooks(userId, form.getTitle(), form.getOrderBy(), form.getPage(), PROFILE_PAGE_SIZE));
        mav.addObject("myBookSearchForm", form);
        mav.addObject("orders", BookSearchOrderBy.values());
        return mav;
    }


    @RequestMapping(method = RequestMethod.GET, path="/editProfile")
    public ModelAndView editProfileForm(@ModelAttribute("editProfileForm") EditProfileForm form, @ModelAttribute("loggedUser") User loggedUser){

        form.setNewFirstName(loggedUser.getFirstName());
        form.setNewLastName(loggedUser.getLastName());
        form.setCbu(loggedUser.getCbu());

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

        us.updateProfile(form.getNewFirstName(),form.getNewLastName(),form.getCbu(), form.getProfilePicture());

        return new ModelAndView("redirect:/profile/"+loggedUser.getUserId());
    }
}
