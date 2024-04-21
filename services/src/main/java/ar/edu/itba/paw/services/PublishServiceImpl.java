package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;


@Service
public class PublishServiceImpl implements PublishService {

    private final BookService bs;
    private final ImageService is;
    private final PdfService ps;
    private final UserService us;

    @Autowired
    public PublishServiceImpl(BookService bs, ImageService is, PdfService ps, UserService us) {
        this.bs = bs;
        this.is = is;
        this.ps = ps;
        this.us = us;
    }

    @Override
    public Book publishBook(
            String writerFirstName,
            String writerLastName,
            String writerEmail,

            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            double price,
            int pageCount,

            MultipartFile image,
            MultipartFile previewPdf
    ) {
        Image bookImage = is.create(image);
        Pdf bookPreviewPdf = ps.create(previewPdf);

        User user = us.create(
                writerFirstName,
                writerLastName,
                writerEmail,
                "idk"
        );

        return bs.create(
                title,
                description,
                genre,
                price,
                pageCount,
                bookPreviewPdf.getPdfId(),
                bookImage.getImageId(),
                suggestedAge,
                user.getUserId(),

                writerFirstName,
                writerLastName,
                writerEmail
        );

    }
}
