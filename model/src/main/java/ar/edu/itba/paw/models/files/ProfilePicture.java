package ar.edu.itba.paw.models.files;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "profile_pictures")
public class ProfilePicture extends File{

    ProfilePicture() {}

    public ProfilePicture(long fileId, byte[] file){
        super(fileId, file);
    }
}
