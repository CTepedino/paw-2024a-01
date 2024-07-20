package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.service.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.users.User;
import ar.edu.itba.paw.models.users.UserRoles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;


@Service
public class PublishServiceImpl implements PublishService {

    private final BookService bs;
    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    public PublishServiceImpl(BookService bs, UserService us) {
        this.bs = bs;
        this.us = us;
    }

    @Transactional
    @Override
    public long publishBook(
            User user,

            String cbu,
            String title,
            String description,
            BookGenre genre,
            int suggestedAge,
            BigDecimal price,
            int pageCount,
            LocalDate publicationDate,

            byte[] cover,
            byte[] preview,
            byte[] bookFile
    ) {


        if (!us.hasRole(UserRoles.WRITER)){
            us.giveWriterRole(cbu);
        }

        long bookId = bs.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                publicationDate,
                user,
                preview,
                cover,
                bookFile
        );

        LOGGER.atDebug().setMessage("Published book: {}").addArgument(title).log();

        return bookId;
    }
}
