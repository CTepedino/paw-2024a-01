package ar.edu.itba.paw.models.exception;

import java.io.Serial;
import java.net.HttpURLConnection;

public class RequestException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1795921399591207338L;


    private final int statusCode;

    public RequestException(int statusCode) {
        super();
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
