package ar.edu.itba.paw.models.files;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class File {

    @Id
    @Column(name = "id")
    private long fileId;

    @Column
    private byte[] file;

    protected File(){}

    File(long fileId, byte[] file){
        this.fileId=fileId;
        this.file=file;
    }


    public long getFileId(){
        return fileId;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }
}
