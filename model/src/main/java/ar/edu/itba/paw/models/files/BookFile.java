package ar.edu.itba.paw.models.files;

public class BookFile extends File{
    public BookFile(long bookFileId, byte[] book){
        super(FileType.PDF, bookFileId, book);
    }
}
