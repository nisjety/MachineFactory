package com.example.maschinefactory.exceptions;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = -4030572821443471682L;
    String resourceName;
    String fieldName;
    long fieldValue;
    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(resourceName+ " not found with " + fieldName+": " + fieldValue);

        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}