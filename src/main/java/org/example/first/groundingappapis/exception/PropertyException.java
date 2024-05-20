package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PropertyException extends RuntimeException {
    private final PropertyErrorResult propertyErrorResult;
    public PropertyException(PropertyErrorResult propertyErrorResult, String message) {
        super(message);
        this.propertyErrorResult = propertyErrorResult;
    }
}