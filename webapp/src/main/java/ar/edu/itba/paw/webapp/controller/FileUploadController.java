package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaces.ImageService;
import ar.edu.itba.paw.interfaces.PdfService;
import ar.edu.itba.paw.models.Image;
import ar.edu.itba.paw.models.Pdf;
import ar.edu.itba.paw.webapp.exception.ImageNotFoundException;
import ar.edu.itba.paw.webapp.exception.PdfNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileNotFoundException;
import java.util.Optional;

@Controller
public class FileUploadController {

    private final ImageService is;
    private final PdfService ps;

    @Autowired
    public FileUploadController(ImageService is, PdfService ps){
        this.is = is;
        this.ps = ps;
    }

    @RequestMapping(method = RequestMethod.GET, path= "/image/{id:\\d+}", produces = {MediaType.IMAGE_PNG_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] getImage(@PathVariable("id") long id){
        Optional<Image> maybeImage = is.findById(id);
        return maybeImage.orElseThrow(ImageNotFoundException::new).getImage();
    }

    @RequestMapping(method = RequestMethod.POST, path = "/uploadImage")
    public ModelAndView uploadImage(@RequestParam("file") MultipartFile file){
        Image image = is.create(file);
        return new ModelAndView("redirect:/viewImage/" + image.getImageId());
    }

    @RequestMapping(method = RequestMethod.GET, path= "/uploadImage")
    public ModelAndView uploadImageForm(){
        return new ModelAndView("uploadImageForm");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/viewImage/{id:\\d+}")
    public ModelAndView imageView(@PathVariable("id") long id){
        ModelAndView mav = new ModelAndView("imageView");
        mav.addObject("imageId", id);
        return mav;
    }

    @RequestMapping(method = RequestMethod.GET, path="/pdf/{id:\\d+}", produces = MediaType.APPLICATION_PDF_VALUE)
    public @ResponseBody byte[] getPdf(@PathVariable("id") long id){
        Optional<Pdf> maybePdf = ps.findById(id);
        return maybePdf.orElseThrow(PdfNotFoundException::new).getPdf();
    }

    @RequestMapping(method = RequestMethod.POST, path= "/uploadPdf")
    public ModelAndView uploadPdf(@RequestParam("file") MultipartFile file){
        Pdf pdf = ps.create(file);
        return new ModelAndView("redirect:/viewPdf/" + pdf.getPdfId());
    }

    @RequestMapping(method = RequestMethod.GET, path= "/uploadPdf")
    public ModelAndView uploadPdfForm(){
        return new ModelAndView("uploadPdfForm");
    }

    @RequestMapping(method = RequestMethod.GET, path = "/viewPdf/{id:\\d+}")
    public ModelAndView pdfView(@PathVariable("id") long id){
        ModelAndView mav = new ModelAndView("pdfView");
        mav.addObject("pdfId", id);
        return mav;
    }
}
