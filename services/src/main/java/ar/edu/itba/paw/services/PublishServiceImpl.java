package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class PublishServiceImpl implements PublishService {

    private final BookService bs;
    private final UserService us;

    @Autowired
    public PublishServiceImpl(BookService bs, UserService us) {
        this.bs = bs;
        this.us = us;
    }

    @Override
    public void publishBook(

            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            double price,
            int pageCount,

            MultipartFile image,
            MultipartFile previewPdf
    ) {
        User user = us.getLoggedUser().orElseThrow(UserNotFoundException::new);

        List<UserRoles> roles = us.getRoles(user.getUserId());

        if (!roles.contains(UserRoles.WRITER)){
            us.giveWriterRole(user.getUserId());
        }

        bs.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                user.getUserId(),
                previewPdf,
                image
        );

    }
}
