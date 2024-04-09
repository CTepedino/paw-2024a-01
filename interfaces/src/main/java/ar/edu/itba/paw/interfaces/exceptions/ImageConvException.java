package ar.edu.itba.paw.interfaces.exceptions;


public class ImageConvException extends CustomizableException {
    public static final int INTERNAL_SERVER_ERROR = 500;
    public ImageConvException() {
        super(INTERNAL_SERVER_ERROR, "exception.imageConversion");
    }

}
