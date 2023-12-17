package com.example.maschinefactory.address;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidAddressDataException extends Exception {

    public InvalidAddressDataException (String message) {
        super(message);
    }
}
