package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.BookPreviewDao;
import ar.edu.itba.paw.models.files.BookPreview;
import org.springframework.stereotype.Repository;

@Repository
public class BookPreviewJpaDao extends FileJpaDao<BookPreview> implements BookPreviewDao {

}
