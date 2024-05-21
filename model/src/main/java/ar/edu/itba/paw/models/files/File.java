package ar.edu.itba.paw.models.files;


public abstract class File {
    private final long fileId;
    private final byte[] file;

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

}
