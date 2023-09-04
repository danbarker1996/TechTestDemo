package uk.co.gamma.address.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CouldNotReadBlacklistException extends RuntimeException {
	
	private static final long serialVersionUID = -8321412256554282255L;

	public CouldNotReadBlacklistException() {
        super("Error trying to read address black list");
    }
}
