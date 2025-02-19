package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.dao.BookDao;
import ar.edu.itba.paw.interfaces.dao.DealDao;
import ar.edu.itba.paw.interfaces.dao.OrderDao;
import ar.edu.itba.paw.interfaces.service.BookService;
import ar.edu.itba.paw.interfaces.service.UserService;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.books.BookGenre;
import ar.edu.itba.paw.models.books.BookSearchOrderBy;
import ar.edu.itba.paw.models.books.*;
import ar.edu.itba.paw.models.PaginatedContent;
import ar.edu.itba.paw.models.exception.*;

import ar.edu.itba.paw.models.orders.OrderStatus;
import ar.edu.itba.paw.models.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
public class BookServiceImpl implements BookService {

    private final BookDao bookDao;
    private final OrderDao orderDao;
    private final DealDao dealDao;

    private final UserService us;

    private final static Logger LOGGER = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    public BookServiceImpl(final BookDao bookDao, final OrderDao orderDao, final DealDao dealDao, final UserService us){
        this.bookDao = bookDao;
        this.orderDao = orderDao;
        this.dealDao = dealDao;
        this.us = us;
    }

    @Transactional
    @Override
    public long create(String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge, LocalDate publishedDate){
        User writer = us.getLoggedUser().orElseThrow(UserNotFoundException::new);

        us.checkWriterRole(writer);
        Book book = bookDao.create(
                title,
                description,
                genre,
                price,
                pageCount,
                suggestedAge,
                publishedDate,
                writer,
                true
        );

        LOGGER.atDebug().setMessage("Created book: {}").addArgument(title).log();
        return book.getBookId();
    }

    @Transactional
    @Override
    public void editPublication(long bookId, String title, String description, BookGenre genre, BigDecimal price, int pageCount, int suggestedAge) {
        Book book = findById(bookId).orElseThrow(BookNotFoundException::new);

        if (book.getDeal() != null && book.getDeal().getPrice().compareTo(price) >= 0){
            dealDao.deleteDeal(bookId);
        }

        bookDao.modify(book, title, description, genre, price, pageCount, suggestedAge);
        LOGGER.atDebug().setMessage("Publication for Book {} edited correctly").addArgument(title).log();
    }

    @Transactional
    @Override
    public void checkBookSalesCategory(Book book){
        long sales = orderDao.getAllOrdersSize(book.getBookId(), null, null, "", OrderStatus.COMPLETED);
        if(sales >= BookSalesCategory.POPULAR.getMinSales()){
            bookDao.updateSalesCategory(book, BookSalesCategory.POPULAR);
        }
        if(sales >= BookSalesCategory.BEST_SELLER.getMinSales()){
            bookDao.updateSalesCategory(book, BookSalesCategory.BEST_SELLER);
        }
    }

    @Transactional
    @Override
    public void setCoverImage(long bookId, byte[] coverImage) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);

        bookDao.createOrUpdateCoverImage(book, coverImage);
        LOGGER.atDebug().setMessage("Cover image for Book {} edited correctly").addArgument(bookId).log();
    }

    @Transactional
    @Override
    public void setPreview(long bookId, byte[] preview) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);

        bookDao.createOrUpdatePreview(book, preview);
        LOGGER.atDebug().setMessage("Preview file for Book {} edited correctly").addArgument(bookId).log();
    }

    @Transactional
    @Override
    public void setBookFile(long bookId, byte[] file) {
        Book book = bookDao.findById(bookId).orElseThrow(BookNotFoundException::new);
        bookDao.unpause(book);

        bookDao.createOrUpdateBookFile(book, file);
        LOGGER.atDebug().setMessage("Book file for Book {} edited correctly").addArgument(bookId).log();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Book> findById(long id) {
        return bookDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAuthor(long userId, long bookId) {
        Optional<Book> maybeBook = bookDao.findById(bookId);
        return maybeBook.filter(b -> b.getWriter().getUserId() == userId).isPresent();
    }

    @Transactional(readOnly = true)
    @Override
    public PaginatedContent<Book> listBooks(BookSearchQueryDTO queryDTO) {
        if (queryDTO.getPageNumber() < 1){
            throw new InvalidPageException();
        }

        if (queryDTO.getOwnerId() != null) {
            queryDTO.setRecommendedByUserOnly(
                    us.getLoggedUser()
                            .filter(u -> u.getUserId() == queryDTO.getOwnerId())
                            .isEmpty());
        }

        if (queryDTO.getRecommendationsForId() != null){
            return getRecommendationsForBook(queryDTO);
        }
        if (queryDTO.getOrderBy()==null){
            queryDTO.setOrderBy(BookSearchOrderBy.PUBLICATION_DATE_DESC);
        }
        return switch(queryDTO.getOrderBy()) {
            case BookSearchOrderBy.NEW_DEALS -> getNewDeals(queryDTO);
            case BookSearchOrderBy.BEST_SELLERS -> getBestSellers(queryDTO);
            default -> searchWithParams(queryDTO);
        };
    }

    private PaginatedContent<Book> getRecommendationsForBook(BookSearchQueryDTO queryDTO){
        List<Book> books;
        long size;
        Optional<Book> book = bookDao.findById(queryDTO.getRecommendationsForId());
        if (book.isEmpty()){
            books = Collections.emptyList();
            size = 0;
        } else {
            books = bookDao.getRecommendationsForBook(queryDTO);
            size = bookDao.getRecommendationsForBookSize(queryDTO);
        }
        return new PaginatedContent<>(books, queryDTO.getPageNumber(), queryDTO.getPageSize(), size);
    }

    private PaginatedContent<Book> getNewDeals(BookSearchQueryDTO queryDTO){
        List<Book> books = bookDao.getBooksWithNewDeals(queryDTO);
        long size = bookDao.getBooksWithNewDealsSize(queryDTO);

        return new PaginatedContent<>(books, queryDTO.getPageNumber(), queryDTO.getPageSize(), size);
    }

    private PaginatedContent<Book> getBestSellers(BookSearchQueryDTO queryDTO){
        List<Book> books = bookDao.getTopBooks(queryDTO);
        long size = bookDao.getTopBooksSize(queryDTO);

        return new PaginatedContent<>(books, queryDTO.getPageNumber(), queryDTO.getPageSize(), size);
    }

    private PaginatedContent<Book> searchWithParams(BookSearchQueryDTO queryDTO){
        List<Book> books = bookDao.searchWithParams(queryDTO);
        long size = bookDao.getSearchSize(queryDTO);

        return new PaginatedContent<>(books, queryDTO.getPageNumber(), queryDTO.getPageSize(), size);
    }
}