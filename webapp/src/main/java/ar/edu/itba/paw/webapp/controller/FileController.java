package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.models.exception.ImageNotFoundException;
import ar.edu.itba.paw.models.exception.PdfNotFoundException;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class FileController {

    private final BookService bs;
    private final OrderService os;

    @Autowired
    public FileController(BookService bs, OrderService os) {
        this.bs = bs;
        this.os = os;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cover/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getImage(@PathVariable("id") long id) {
        return bs.getCover(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/preview/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPdf(@PathVariable("id") long id) {
        return bs.getPreview(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/receipt/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getReceipt(@PathVariable("id") long id) {
        return os.getReceipt(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/books/file/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getBookFile(@PathVariable("id") long id){
        return bs.getBookFile(id).getFile();
    }

}
