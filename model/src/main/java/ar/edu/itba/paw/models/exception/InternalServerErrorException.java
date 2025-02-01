package ar.edu.itba.paw.models.exception;

import java.io.Serial;
import java.net.HttpURLConnection;

public class InternalServerErrorException extends RequestException {

    @Serial
    private static final long serialVersionUID = 5932903084507611631L;

    public InternalServerErrorException() {
        super(HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
}
