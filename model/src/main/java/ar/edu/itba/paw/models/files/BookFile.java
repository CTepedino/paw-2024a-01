package ar.edu.itba.paw.models.files;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "book_files")
public class BookFile extends File{

    protected BookFile() {}

    public BookFile(long bookFileId, byte[] book){
        super(bookFileId, book);
    }
}
