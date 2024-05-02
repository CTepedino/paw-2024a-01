package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.PdfDao;
import ar.edu.itba.paw.interfaces.PdfService;
import ar.edu.itba.paw.models.Pdf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PdfServiceImpl implements PdfService {

    private final PdfDao pdfDao;

    @Autowired
    public PdfServiceImpl(PdfDao pdfDao){
        this.pdfDao = pdfDao;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Pdf> findById(long id) {
        return pdfDao.findById(id);
    }

    @Transactional
    @Override
    public Pdf create(MultipartFile pdf) {
        try {
            return pdfDao.create(pdf.getBytes());
        } catch (IOException e){
            return null;
        }
    }
}
