package ar.edu.itba.paw.models.exception;

import java.io.Serial;
import java.net.HttpURLConnection;

public class NotFoundException extends RequestException {
    @Serial
    private static final long serialVersionUID = -6279678494012585680L;

    public NotFoundException() {
        super(HttpURLConnection.HTTP_NOT_FOUND);
    }
}
