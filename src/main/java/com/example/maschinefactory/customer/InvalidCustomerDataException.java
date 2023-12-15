package com.example.maschinefactory.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidCustomerDataException extends Exception {

    public InvalidCustomerDataException(String message) {
        super(message);
    }
}
