package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PropertyErrorResult {

    PROPERTY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매물입니다."), //404
    ;

    private final HttpStatus httpStatus;
    private final String message;
}
