package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PdfService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pdf;
import ar.edu.itba.paw.models.exception.ImageNotFoundException;
import ar.edu.itba.paw.models.exception.PdfNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class FileController {

    private final ImageService is;
    private final PdfService ps;

    @Autowired
    public FileController(ImageService is, PdfService ps) {
        this.is = is;
        this.ps = ps;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/image/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getImage(@PathVariable("id") long id) {
        Optional<Image> maybeImage = is.findById(id);
        return maybeImage.orElseThrow(ImageNotFoundException::new).getImage();
    }

    @RequestMapping(method = RequestMethod.GET, path = "/pdf/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPdf(@PathVariable("id") long id) {
        Optional<Pdf> maybePdf = ps.findById(id);
        return maybePdf.orElseThrow(PdfNotFoundException::new).getPdf();
    }

}
