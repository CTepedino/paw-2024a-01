package ar.edu.itba.paw.persistence.files;

import ar.edu.itba.paw.interfaces.dao.files.CoverImageDao;
import ar.edu.itba.paw.models.files.CoverImage;
import org.springframework.stereotype.Repository;

@Repository
public class CoverImageJpaDao extends FileJpaDao<CoverImage> implements CoverImageDao {


}
