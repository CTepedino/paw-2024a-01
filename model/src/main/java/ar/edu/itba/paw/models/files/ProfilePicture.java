package ar.edu.itba.paw.models.files;

public class ProfilePicture extends File{
    public ProfilePicture(long fileId, byte[] file){
        super(FileType.IMAGE, fileId, file);
    }
}
