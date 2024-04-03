package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.models.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class BookController {

    private final BookService bs;

    @Autowired
    public BookController(final BookService bs){
        this.bs = bs;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/addbook")
    public ModelAndView otherView(){
        final ModelAndView mav = new ModelAndView("addBook");
        return mav;
    }

    @RequestMapping(method=RequestMethod.POST, path ="/addbook")
    public ModelAndView addBook(@RequestParam("title") String  title,@RequestParam("description") String description,
                                @RequestParam("genra") String genra,
                                @RequestParam("price") double price,
                                @RequestParam("page_numbers") int pageNumbers,
                                @RequestParam("prev") String prev,
                                @RequestParam("image") String image,
                                @RequestParam("suggested_age") int suggestedAge,
                                @RequestParam("published_date") String publishedDate,
                                @RequestParam("writer_email") String writerEmail)
    {
        final Book book = bs.create(title,description,genra,price,pageNumbers,prev,image,suggestedAge,publishedDate,writerEmail);
        return new ModelAndView("redirect:/");
    }
}
