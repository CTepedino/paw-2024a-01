package ar.edu.itba.paw.models.files;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "cover_images")
public class CoverImage extends File{

    protected CoverImage() {}

    public CoverImage(long imageId, byte[] coverImage){
        super(imageId, coverImage);
    }
}
