package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.exception.ImageNotFoundException;
import ar.edu.itba.paw.models.exception.PdfNotFoundException;
import ar.edu.itba.paw.models.files.CoverImage;
import ar.edu.itba.paw.models.files.ProfilePicture;
import ar.edu.itba.paw.models.users.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
public class FileController {

    private final BookService bs;
    private final UserService us;

    @Autowired
    public FileController(BookService bs, UserService us) {
        this.bs = bs;
        this.us = us;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/image/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getImage(@PathVariable("id") long id) {
        return bs.getCover(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/pdf/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPdf(@PathVariable("id") long id) {
        return bs.getPreview(id).getFile();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/profilePicture/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getProfileImage(@PathVariable("id") long id) {
        return us.getProfilePictureOrDefault(id).getFile();
    }


}
