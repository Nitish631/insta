package com.nitish.insta.Exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException{
    String resourceName;
    String fieldName;
    long fieldValue;
    String stringFieldValue;
    public ResourceNotFoundException(String resourceName, String fieldName, long fieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public ResourceNotFoundException(String resourceName, String fieldName, String stringFieldValue) {
        super(String.format("%s not found with %s: %s", resourceName, fieldName, stringFieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.stringFieldValue = stringFieldValue;
    }
}
