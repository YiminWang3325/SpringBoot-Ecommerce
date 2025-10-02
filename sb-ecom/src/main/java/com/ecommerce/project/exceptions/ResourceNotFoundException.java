package com.ecommerce.project.exceptions;

public class ResourceNotFoundException extends RuntimeException {
     String resourceName;
     String fieldName;
     Long fieldId;
     String field;

    public ResourceNotFoundException(String resourceName,  String field, Long fieldId) {
        super(String.format(" %s not found in  %s with %d", resourceName, field,fieldId));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldId = fieldId;
    }

    public ResourceNotFoundException(String resourceName,  String field, String fieldName ) {
        super(String.format(" %s not found in  %s with %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }
}
