package uk.co.gamma.address.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AddressNotFoundException extends RuntimeException {

    private final Integer id;

    public AddressNotFoundException(Integer id) {
        super("Address with ID %d not found".formatted(id));
        this.id = id;
    }

    public Integer getId() {
        return id;
    }
}