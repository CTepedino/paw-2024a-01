package ar.edu.itba.paw.services;


import ar.edu.itba.paw.interfaces.WriterDao;
import ar.edu.itba.paw.interfaces.WriterService;
import ar.edu.itba.paw.models.Writer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class WriterServiceImpl implements WriterService {

    private final WriterDao writerDao;

    @Autowired
    public WriterServiceImpl(final WriterDao writerDao){
        this.writerDao=writerDao;
    }

    @Override
    public Optional<Writer> findById(long id) {
        return writerDao.findById(id);
    }

    @Override
    public Writer create(String name, String lastName, String email) {
        return writerDao.create(name, lastName, email);
    }
}
