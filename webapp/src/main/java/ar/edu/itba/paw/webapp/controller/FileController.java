package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.interfaces.service.OrderService;
import ar.edu.itba.paw.models.exception.ImageNotFoundException;
import ar.edu.itba.paw.models.exception.PdfNotFoundException;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.files.PaymentReceipt;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class FileController {

    private final BookService bs;
    private final OrderService os;
    private final UserService us;

    @Autowired
    public FileController(BookService bs, OrderService os, UserService us) {
        this.bs = bs;
        this.os = os;
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/cover/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getImage(@PathVariable("id") long id) {
        return bs.getCover(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/preview/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPdf(@PathVariable("id") long id) {
        return bs.getPreview(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/book/file/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getBookFile(@PathVariable("id") long id){
        return bs.getBookFile(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/profilePicture/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getProfileImage(@PathVariable("id") long id) {
        return us.getProfilePictureOrDefault(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/receipt/{id:\\d+}")
    public ResponseEntity<byte[]> getReceipt(@PathVariable("id") long id) {
        PaymentReceipt receipt = os.getReceipt(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(receipt.getType()));
        return new ResponseEntity<>(receipt.getFile(), headers, HttpStatus.OK);
    }

}
