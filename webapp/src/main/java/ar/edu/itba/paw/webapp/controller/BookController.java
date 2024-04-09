package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.BookService;
import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.WriterService;
import ar.edu.itba.paw.models.Book;
import ar.edu.itba.paw.models.FormDataWithFile;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

@Controller
public class BookController {

    private final BookService bs;
    private final WriterService ws;
    private final ImageService is;

    @Autowired
    public BookController(BookService bs, WriterService ws, ImageService is){
        this.bs = bs;
        this.ws = ws;
        this.is = is;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/")
    public ModelAndView home(){
        final ModelAndView mav = new ModelAndView("home");
        mav.addObject("books", bs.getBooks());
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/addbook")
    public ModelAndView otherView(){
        final ModelAndView mav = new ModelAndView("addBook");
        return mav;
    }

    @RequestMapping(method = RequestMethod.POST,path ="/addbook")
    public ModelAndView addBook(@ModelAttribute FormDataWithFile formDataWithFile, ModelMap modelMap) throws IOException {
        modelMap.addAttribute("formDateWithFile",formDataWithFile);
      /*  System.out.println("Contenido del objeto FormDataWithFile: " + formDataWithFile.toString());
        System.out.println("File: " + formDataWithFile.getFile());
        System.out.println("Writer Name: " + formDataWithFile.getWriter_name());
        System.out.println("Last Name: " + formDataWithFile.getWriter_lastname()); */

        final Image image = is.uploadImage(formDataWithFile.getFile().getInputStream().readAllBytes());
        //final Writer writer = ws.create(formDataWithFile.getWriter_name(), formDataWithFile.getWriter_lastname(),formDataWithFile.getWriter_email());
        final Book book = bs.create(formDataWithFile.getTitle(), formDataWithFile.getDescription(), formDataWithFile.getGenre(),formDataWithFile.getPrice(),formDataWithFile.getPage_numbers(), formDataWithFile.getPrev(), image.getImageId(), formDataWithFile.getSuggested_age(), formDataWithFile.getPublished_date(), formDataWithFile.getWriter_name(), formDataWithFile.getWriter_lastname(),formDataWithFile.getWriter_email());
        return new ModelAndView("redirect:/");
    }
}
