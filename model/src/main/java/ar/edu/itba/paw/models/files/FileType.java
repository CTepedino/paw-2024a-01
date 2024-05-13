package ar.edu.itba.paw.models.files;

public enum FileType {
    PDF("application/pdf"),
    IMAGE("image/*");

    private final String type;

    FileType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
