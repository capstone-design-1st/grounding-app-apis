package org.example.first.groundingappapis.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PropertyErrorResult {

    PROPERTY_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 매물입니다."), //404
    INVALID_SORT_TYPE(HttpStatus.BAD_REQUEST, "유효하지 않은 정렬 타입입니다."), //400;
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 누른 매물입니다."),
    NOT_LIKED(HttpStatus.BAD_REQUEST, "좋아요를 누르지 않은 매물입니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
