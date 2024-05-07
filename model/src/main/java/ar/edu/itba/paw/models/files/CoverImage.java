package ar.edu.itba.paw.models.files;

public class CoverImage extends File{
    public CoverImage(long imageId, byte[] coverImage){
        super(FileType.IMAGE,imageId, coverImage);
    }
}
