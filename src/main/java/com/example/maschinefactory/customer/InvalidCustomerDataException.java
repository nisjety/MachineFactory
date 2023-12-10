package com.example.maschinefactory.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class InvalidCustomerDataException extends Exception {
}
