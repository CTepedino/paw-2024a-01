package ar.edu.itba.paw.models.files;

public abstract class File {
    private final FileType fileType;
    private final long fileId;
    private final byte[] file;

    File(FileType fileType, long fileId, byte[] file){
        this.fileType = fileType;
        this.fileId=fileId;
        this.file=file;
    }

    public String getFileType(){
        return fileType.getType();
    }

    public long getFileId(){
        return fileId;
    }

    public byte[] getFile() {
        return file;
    }
}
