package ar.edu.itba.paw.models.files;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "book_previews")
public class BookPreview extends File{

    BookPreview() {}

    public BookPreview(long previewId, byte[] preview){
        super(previewId, preview);
    }
}
