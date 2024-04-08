package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Pdf;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface PdfService {
    Optional<Pdf> findById(long id);

    Pdf create(MultipartFile pdf);
}
