package ar.edu.itba.paw.models.exception;

import java.io.Serial;
import java.net.HttpURLConnection;

public class BadRequestException extends RequestException {

    @Serial
    private static final long serialVersionUID = -2112850700788064142L;

    public BadRequestException() {
        super(HttpURLConnection.HTTP_BAD_REQUEST);
    }
}
