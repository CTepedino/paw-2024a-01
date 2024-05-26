package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookFileDao;
import ar.edu.itba.paw.models.books.Book;
import ar.edu.itba.paw.models.files.BookFile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
public class BookFileJpaDao extends FileJpaDao<BookFile> implements BookFileDao {

}
