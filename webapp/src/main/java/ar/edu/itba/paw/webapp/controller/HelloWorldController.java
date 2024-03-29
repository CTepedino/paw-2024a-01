package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloWorldController {

    private final UserService us;

    @Autowired
    public HelloWorldController(final UserService us){
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView registerForm(){
        final ModelAndView mav = new ModelAndView("registerForm");

        return mav;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/create")
    public ModelAndView register(@RequestParam("username") String username){
        final User user = us.create(username);
        return new ModelAndView("redirect:/" + user.getUserId());
    }

    @RequestMapping(method = RequestMethod.GET, path="/{userId:\\d+}")
    public ModelAndView userProfile(@PathVariable("userId") final long userId){
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user", us.findById(userId).orElseThrow(UserNotFoundException::new));

        return mav;
    }



    @RequestMapping(method = RequestMethod.GET, path="/{nonnumeric:[a-z]+}")
    public ModelAndView invalidView(){
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user", us.findById(-1).orElseThrow(UserNotFoundException::new));

        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/other")
    public ModelAndView otherView(){
        final ModelAndView mav = new ModelAndView("anotherPage");

        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/dynamic")
    public ModelAndView dynamicView(){
        final ModelAndView mav = new ModelAndView("dynamicPage");
        mav.addObject("title", "Dynamic Page");
        User testUser = new User(2, "test user");
        mav.addObject("user", testUser);
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/tag-lib")
    public ModelAndView taglibView(){
        final ModelAndView mav = new ModelAndView("dynamicPageWithTaglib");
        mav.addObject("title", "Dynamic Page");
        User testUser = new User(2, "test user");
        mav.addObject("user", testUser);
        return mav;
    }
}
